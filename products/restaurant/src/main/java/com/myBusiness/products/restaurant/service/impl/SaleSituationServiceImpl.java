package com.myBusiness.products.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.dao.main.OrderMapper;
import com.myBusiness.products.restaurant.dao.main.SaleSituationDetailMapper;
import com.myBusiness.products.restaurant.dao.main.SaleSituationSummaryMapper;
import com.myBusiness.products.restaurant.dao.orderDetail.SaleCommentMapper;
import com.myBusiness.products.restaurant.entity.OrderEntity;
import com.myBusiness.products.restaurant.entity.SaleCommentEntity;
import com.myBusiness.products.restaurant.entity.SaleSituationDetailEntity;
import com.myBusiness.products.restaurant.entity.SaleSituationSummaryEntity;
import com.myBusiness.products.restaurant.enums.OrderStatus;
import com.myBusiness.products.restaurant.service.SaleSituationService;
import com.myBusiness.products.restaurant.util.CommonUtil;
import com.myBusiness.products.restaurant.vo.OrderGoodsInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SaleSituationServiceImpl implements SaleSituationService {

    @Autowired
    SaleSituationDetailMapper saleSituationDetailMapper;

    @Autowired
    SaleSituationSummaryMapper saleSituationSummaryMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    SaleCommentMapper saleCommentMapper;

    @Override
    public ResponseResult getGoodsSaleSituation(int goodsId, LocalDate startDate, LocalDate endDate) {
        if(startDate != null && endDate != null){
            if(startDate.isAfter(endDate)){
                return ResponseResult.error("起始日期不能大于结束日期");
            }
            List<SaleSituationDetailEntity> saleSituationList = saleSituationDetailMapper.selectList(
                    new QueryWrapper<SaleSituationDetailEntity>()
                            .lambda()
                            .eq(SaleSituationDetailEntity::getGoodsId, goodsId)
                            .le(SaleSituationDetailEntity::getDate, endDate)
                            .ge(SaleSituationDetailEntity::getDate, startDate));
            return ResponseResult.success(saleSituationList);
        }else{
            LocalDate requestDate = startDate == null ? endDate : null;
            if(requestDate == null){
                SaleSituationSummaryEntity saleSituation = saleSituationSummaryMapper.selectById(goodsId);
                return ResponseResult.success(saleSituation);
            }else{
                SaleSituationDetailEntity saleSituation = saleSituationDetailMapper.selectOne(
                        new QueryWrapper<SaleSituationDetailEntity>()
                                .lambda()
                                .eq(SaleSituationDetailEntity::getGoodsId, goodsId)
                                .eq(SaleSituationDetailEntity::getDate, requestDate));
                return ResponseResult.success(saleSituation);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,value = "mainDatasourceTransactionManager")
    public void updateSaleSituation(List<SituationObj> situationObjList) {
        for(SituationObj obj : situationObjList){
            saleSituationDetailMapper.updateSituation(obj);
            saleSituationSummaryMapper.updateSaleSituationSummary(obj);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,value = "mainDatasourceTransactionManager")
    public ResponseResult evaluateGoodsInOrder(long orderId, int goodsId, int score, String comment) throws Exception {
        OrderEntity orderEntity = orderMapper.selectById(orderId);
        if(orderEntity == null){
            throw new Exception("订单不存在");
        }

        if(!OrderStatus.COMPLETE.getCode().equals(orderEntity.getStatus())){
            throw new Exception("订单状态错误");
        }

        Integer count = saleCommentMapper.selectCount(new QueryWrapper<SaleCommentEntity>()
                .lambda().eq(SaleCommentEntity::getGoodsId, goodsId)
                .eq(SaleCommentEntity::getOrderId, orderId));
        if(count != null && count > 0){
            throw new Exception("该商品已经被评价过了");
        }

        SaleCommentEntity saleCommentEntity = new SaleCommentEntity();
        saleCommentEntity.setOrderId(orderId);
        saleCommentEntity.setGoodsId(goodsId);
        saleCommentEntity.setScore(score);
        saleCommentEntity.setCommentDate(LocalDate.now());
        saleCommentEntity.setComment(comment);

        saleCommentMapper.insert(saleCommentEntity);

        return ResponseResult.success();
    }


    @Override
    public SituationObj genUpdateSituationObj(int goodsId, LocalDate localDate, int amount,int score) {
        return new SituationObj(goodsId,localDate,amount,score);
    }

    @Override
    public ResponseResult reviewGoodsInOrder(Long orderId, Integer goodsId, String review) {
        Integer count = saleCommentMapper.selectCount(new QueryWrapper<SaleCommentEntity>()
                .lambda().eq(SaleCommentEntity::getGoodsId, goodsId)
                .eq(SaleCommentEntity::getOrderId, orderId));
        if(count == null || count == 0){
            return ResponseResult.error("该商品未被评价过");
        }

        int update = saleCommentMapper.update(new SaleCommentEntity(),
                new UpdateWrapper<SaleCommentEntity>()
                        .lambda()
                        .set(SaleCommentEntity::getReviewDate, LocalDate.now())
                        .set(SaleCommentEntity::getReview, review)
                        .eq(SaleCommentEntity::getGoodsId, goodsId)
                        .eq(SaleCommentEntity::getOrderId, orderId));

        if(update > 0){
            return ResponseResult.success();
        }

        return ResponseResult.error("评价失败");
    }

    @Override
    public ResponseResult queryEvaluateByOrderId(long orderId) {
        OrderEntity orderEntity = orderMapper.selectOne(
                new QueryWrapper<OrderEntity>()
                        .lambda()
                        .eq(OrderEntity::getOrderId, orderId)
                        .select(OrderEntity::getOrderId)
                        .select(OrderEntity::getOrderItem));
        if(orderEntity == null){
            return ResponseResult.error("该订单不存在");
        }
        List<OrderGoodsInfoVo> goodsList = CommonUtil.getOrderGoodsInfoVoFromJson(orderEntity.getOrderItem());
        List<SaleCommentEntity> saleCommentList = new ArrayList<>();
        for(OrderGoodsInfoVo vo : goodsList){
            int goodsId = vo.getGoodsId();
            SaleCommentEntity saleCommentEntity = queryEvaluateById(goodsId,orderId);

            Optional.ofNullable(saleCommentEntity)
                    .ifPresent(entity -> saleCommentList.add(saleCommentEntity));

        }
        return ResponseResult.success(saleCommentList);
    }

    @Override
    public SaleCommentEntity queryEvaluateById(int goodsId, long orderId){
        return saleCommentMapper.selectOne(
                new QueryWrapper<SaleCommentEntity>()
                        .lambda()
                        .eq(SaleCommentEntity::getGoodsId, goodsId)
                        .eq(SaleCommentEntity::getOrderId, orderId));
    }

    @Override
    public List<SaleCommentEntity> queryEvaluateByGoodsId(int goodsId, Long minOrderId, int limit) {
        LambdaQueryWrapper<SaleCommentEntity> lambda = new QueryWrapper<SaleCommentEntity>()
                .lambda()
                .eq(SaleCommentEntity::getGoodsId, goodsId)
                .orderByDesc(SaleCommentEntity::getOrderId);
        if(minOrderId != null){
            lambda.lt(SaleCommentEntity::getOrderId, minOrderId);
        }
        return saleCommentMapper.selectList(lambda);
    }

}
