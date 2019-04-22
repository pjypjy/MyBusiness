package com.myBusiness.products.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.common.util.CommonUtil;
import com.myBusiness.products.restaurant.config.token.TokenManager;
import com.myBusiness.products.restaurant.config.token.UserToken;
import com.myBusiness.products.restaurant.entity.CouponEntity;
import com.myBusiness.products.restaurant.dao.main.CouponMapper;
import com.myBusiness.products.restaurant.service.CouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pjy
 * @since 2018-09-26
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, CouponEntity> implements CouponService {

    @Autowired
    CouponMapper couponMapper;

    @Autowired
    TokenManager tokenManager;

    @Override
    public ResponseResult disableCoupon(long couponId) {
        CouponEntity couponEntity = couponMapper.selectById(new QueryWrapper<CouponEntity>()
                .lambda()
                .select(CouponEntity::getCouponId)
                .select(CouponEntity::getStatus)
                .eq(CouponEntity::getCouponId, couponId));
        if(couponEntity == null){
            return ResponseResult.error("该优惠券不存在");
        }
        if("0005-4".equals(couponEntity.getType())){
            return ResponseResult.error("该优惠券已经被禁用");
        }
        if("0005-3".equals(couponEntity.getType())){
            return ResponseResult.error("该优惠券已经被使用,不允许操作");
        }
        couponEntity.setType("0005-4");
        boolean b = couponMapper.updateById(couponEntity) > 0;
        return b ? ResponseResult.success() : ResponseResult.error("禁用失败");
    }

    @Override
    public ResponseResult delete(long couponId) {
        CouponEntity couponEntity = couponMapper.selectById(new QueryWrapper<CouponEntity>()
                .lambda()
                .select(CouponEntity::getCouponId)
                .select(CouponEntity::getStatus)
                .eq(CouponEntity::getCouponId, couponId));
        if(couponEntity == null){
            return ResponseResult.error("该优惠券不存在");
        }
        if("0005-3".equals(couponEntity.getType())){
            return ResponseResult.error("该优惠券已经被使用,不允许操作");
        }
        boolean b = couponMapper.deleteById(couponId) > 0;
        return b ? ResponseResult.success() : ResponseResult.error("删除失败");
    }

    @Override
    public ResponseResult distributeDeleteIfExpire(long couponId, long userId) {
        CouponEntity couponEntity = couponMapper.selectOne(new QueryWrapper<CouponEntity>()
                .lambda()
                .select(CouponEntity::getCouponId)
                .select(CouponEntity::getStatus)
                .eq(CouponEntity::getCouponId,couponId));

        if(couponEntity == null){
            return ResponseResult.error("该优惠券不存在");
        }
        if("0005-3".equals(couponEntity.getStatus())){
            return ResponseResult.error("该优惠券已使用");
        }
        if("0005-2".equals(couponEntity.getStatus())){
            return ResponseResult.error("该优惠券已被发放");
        }
        if(couponEntity.getExpireDate().isBefore(LocalDate.now())){
//            delete(couponId);
            return ResponseResult.error("该优惠券已过期,自动删除...");
        }
        couponEntity.setStatus("0005-2");
        couponEntity.setAuthorUserId(userId);
        UserToken userToken = tokenManager.get(CommonUtil.getTokenString());
        couponEntity.setDistributeUserId(userToken.getUserId());
        return couponMapper.updateById(couponEntity) > 0 ? ResponseResult.success() : ResponseResult.error("分配失败");
    }

    @Override
    public ResponseResult getByIdDeleteIfExpire(long couponId) {
        CouponEntity couponEntity = couponMapper.selectById(couponId);
        if(couponEntity == null){
            return ResponseResult.error("该优惠券不存在");
        }
        if(couponEntity.getExpireDate().isBefore(LocalDate.now())){
            delete(couponId);
            return ResponseResult.error("该优惠券已过期,自动删除...");
        }
        return ResponseResult.success(couponEntity);
    }
}
