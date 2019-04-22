package com.myBusiness.products.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.myBusiness.common.exception.CouponException;
import com.myBusiness.common.exception.GoodsInfoExpireException;
import com.myBusiness.common.exception.LackOfStockException;
import com.myBusiness.common.exception.MyMenuNotFoundException;
import com.myBusiness.common.util.IdUtil;
import com.myBusiness.products.restaurant.config.token.TokenManager;
import com.myBusiness.products.restaurant.dao.main.*;
import com.myBusiness.products.restaurant.entity.*;
import com.myBusiness.products.restaurant.rabbitClient.OrderCloseSender;
import com.myBusiness.products.restaurant.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myBusiness.products.restaurant.util.CommonUtil;
import com.myBusiness.products.restaurant.vo.GoodsInfoVo;
import com.myBusiness.products.restaurant.vo.OrderGoodsInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pjy
 * @since 2018-10-22
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderService {

    @Autowired
    MyMenuMapper myMenuMapper;

    @Autowired
    TokenManager tokenManager;

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    CouponMapper couponMapper;

    @Autowired
    OrderCancelMapper orderCancelMapper;

    @Autowired
    OrderCloseSender orderCloseSender;

    @Transactional(rollbackFor = Exception.class,value = "mainDatasourceTransactionManager")
    @Override
    public OrderEntity generateOrderFromMyMenu(int index, String couponId,String orderRemark,int customerCount) throws GoodsInfoExpireException, LackOfStockException, CouponException, MyMenuNotFoundException {
        MyMenuEntity myMenuEntity = myMenuMapper.selectOne(
                new QueryWrapper<MyMenuEntity>()
                        .lambda()
                        .select(MyMenuEntity::getGoodsInfo)
                        .eq(MyMenuEntity::getUserId,tokenManager.getUserId())
                        .eq(MyMenuEntity::getIndex,index));

        if(myMenuEntity == null){
            throw new MyMenuNotFoundException();
        }
        String goodsInfo = myMenuEntity.getGoodsInfo();
        List<GoodsInfoVo> goodsInfoVoList = CommonUtil.getGoodsInfoVoFromJson(goodsInfo);
        List<OrderGoodsInfoVo> orderGoodsInfoVoList = new ArrayList<>();

        //所有商品原始价格之和
        BigDecimal originalMoney = new BigDecimal(0);
        //所有商品店铺优惠价之和
        BigDecimal needPayMoney = new BigDecimal(0);
        for(GoodsInfoVo goodsInfoVo : goodsInfoVoList){
            int goodsId = goodsInfoVo.getGoodsId();
            int amount = goodsInfoVo.getAmount();
            if(amount == 0){
                continue;
            }
            GoodsEntity goodsEntity = goodsMapper.selectById(goodsId);
            if(goodsEntity == null){
                throw new GoodsInfoExpireException();
            }

            //检查库存
            Boolean enableAmount = goodsEntity.getEnableAmount();
            if(enableAmount){
                int i = goodsMapper.changeAmount(goodsId, amount * (-1));
                if(i == 0){
                    throw new LackOfStockException(goodsId,goodsEntity.getName());
                }
            }

            //订单的item
            OrderGoodsInfoVo orderGoodsInfo = new OrderGoodsInfoVo();
            orderGoodsInfo.setGoodsId(goodsId);
            orderGoodsInfo.setAmount(amount);
            orderGoodsInfo.setOriginalPrice(goodsEntity.getPrice());

            //商品的原价
            originalMoney = originalMoney.add(new BigDecimal(amount).multiply(goodsEntity.getPrice()));

            //商品是否打折后的价格
            Boolean enableDiscount = goodsEntity.getEnableDiscount();
            if(enableDiscount){
                BigDecimal goodsRealPrice = getGoodsRealPrice(goodsEntity);
                needPayMoney = needPayMoney.add(goodsRealPrice);
                orderGoodsInfo.setRealPrice(goodsRealPrice);
            }else{
                needPayMoney = needPayMoney.add(goodsEntity.getPrice());
                orderGoodsInfo.setRealPrice(goodsEntity.getPrice());
            }
            orderGoodsInfoVoList.add(orderGoodsInfo);

        }
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(IdUtil.genOrderId());
        //订单原始价格(不含任何打折和优惠券)
        orderEntity.setOriginalMoney(originalMoney);
        //商品打折之后的价格(不含优惠券)
        orderEntity.setNeedPayMoney(needPayMoney);

        //计算使用优惠券之后，最终需支付的价格
        //finalPayMoney = needPayMoney - 优惠券
        BigDecimal finalPayMoney = null;
        if(CommonUtil.isNotEmpty(couponId)){
            CouponEntity couponEntity = couponMapper.selectById(couponId);

            //检查优惠券是否存在、是否未被使用、是否达到要求的金额
            if(couponEntity == null
                    || !couponEntity.getStatus().equals("0005-2")
                    || couponEntity.getStartDate().isAfter(LocalDate.now())
                    || couponEntity.getExpireDate().isBefore(LocalDate.now())
                    || couponEntity.getRequireMoney() != null && couponEntity.getRequireMoney().compareTo(needPayMoney) > 0){
                throw new CouponException();
            }
            String couponType = couponEntity.getType();
            switch (couponType){
                case "0004-1":
                    //抵用券，从 需支付的价格 减去优惠金额，再和0比较
                    finalPayMoney = needPayMoney.subtract(couponEntity.getOffsetMoney());
                    if(finalPayMoney.compareTo(CommonUtil.getBigDecimalOf_0()) < 0){
                        finalPayMoney = CommonUtil.getBigDecimalOf_0();
                    }
                    break;
                case "0004-2":
                    //折扣券，直接在 需支付的价格 上打折计算
                    finalPayMoney = couponEntity.getDiscount().divide(CommonUtil.getBigDecimalOf_10()).multiply(needPayMoney);
                    break;
                default:
                    //类型错误
                    throw new CouponException();
            }
            orderEntity.setCouponId(couponId);

            couponMapper.update(new CouponEntity(),
                    new UpdateWrapper<CouponEntity>()
                            .lambda()
                            .set(CouponEntity::getStatus,"0005-3")
                            .eq(CouponEntity::getCouponId,couponId));

        }else{
            //没有使用优惠券，订单实际需支付的价格就是 需支付的价格（need_pay_money）
            finalPayMoney = needPayMoney;
        }

        //最终需要支付的价格
        orderEntity.setFinalPayMoney(finalPayMoney);

        orderEntity.setOrderItem(CommonUtil.object2JsonString(orderGoodsInfoVoList));
        orderEntity.setOrderRemark(orderRemark);
        orderEntity.setCustomerCount(customerCount);
        orderEntity.setUserId(tokenManager.getUserId());
        boolean save = save(orderEntity);
        if(save){
            orderCloseSender.send(orderEntity.getOrderId());
        }else{
            throw new RuntimeException("生成订单失败");
        }
        return orderEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class,value = "mainDatasourceTransactionManager")
    public OrderCancelEntity cancelOrder(long orderId, String cancelReason, String cancelRemark) throws Exception {

        long cancelId = IdUtil.genCancelId(orderId);
        OrderCancelEntity orderCancelEntity = new OrderCancelEntity();
        orderCancelEntity.setCancelId(cancelId);
        orderCancelEntity.setCancelReason(cancelReason);
        orderCancelEntity.setCancelRemark(cancelRemark);
        int insert = orderCancelMapper.insert(orderCancelEntity);

        if(insert > 0){
            int update = orderCancelMapper.update(new OrderCancelEntity(),
                    new UpdateWrapper<OrderCancelEntity>()
                            .lambda()
                            .set(OrderCancelEntity::getCancelId, cancelId));
            if(update > 0){
                return orderCancelEntity;
            }
        }
        throw new Exception("取消订单失败");
    }

    @Override
    public BigDecimal getGoodsRealPrice(GoodsEntity goodsEntity){
        // 商品实际价格 = 折扣 / 10 * 原价
        return goodsEntity.getDiscount().divide(CommonUtil.getBigDecimalOf_10()).multiply(goodsEntity.getPrice());
    }
}
