package com.myBusiness.products.restaurant.service;

import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.entity.CouponEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pjy
 * @since 2018-09-26
 */
public interface CouponService extends IService<CouponEntity> {

    public ResponseResult disableCoupon(long couponId);

    public ResponseResult delete(long couponId);

    public ResponseResult distributeDeleteIfExpire(long couponId, long userId);

    public ResponseResult getByIdDeleteIfExpire(long couponId);

}
