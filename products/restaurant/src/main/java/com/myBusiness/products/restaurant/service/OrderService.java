package com.myBusiness.products.restaurant.service;

import com.myBusiness.common.exception.CouponException;
import com.myBusiness.common.exception.GoodsInfoExpireException;
import com.myBusiness.common.exception.LackOfStockException;
import com.myBusiness.common.exception.MyMenuNotFoundException;
import com.myBusiness.products.restaurant.entity.GoodsEntity;
import com.myBusiness.products.restaurant.entity.OrderCancelEntity;
import com.myBusiness.products.restaurant.entity.OrderEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pjy
 * @since 2018-10-22
 */
public interface OrderService extends IService<OrderEntity> {

    public OrderEntity generateOrderFromMyMenu(int index, String couponId,String orderRemark,int customerCount) throws GoodsInfoExpireException, LackOfStockException, CouponException, MyMenuNotFoundException;

    public OrderCancelEntity cancelOrder(long orderId, String cancelReason, String cancelRemark) throws Exception;

    /**
     * 商品实际价格
     * @param goodsEntity
     * @return
     */
    public BigDecimal getGoodsRealPrice(GoodsEntity goodsEntity);
}
