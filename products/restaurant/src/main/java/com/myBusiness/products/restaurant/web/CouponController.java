package com.myBusiness.products.restaurant.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.common.util.CommonUtil;
import com.myBusiness.common.util.IdUtil;
import com.myBusiness.products.restaurant.config.token.TokenManager;
import com.myBusiness.products.restaurant.config.token.UserToken;
import com.myBusiness.products.restaurant.entity.CouponEntity;
import com.myBusiness.products.restaurant.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pjy
 * @since 2018-09-26
 */
@Api(description = "优惠券管理")
@RestController
@Validated
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    CouponService couponService;

    @Autowired
    TokenManager tokenManager;

    @ApiOperation(value = "添加/编辑 优惠券" ,httpMethod = "POST")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResponseResult save(
            @ApiParam(name="couponId",value = "优惠券id",required = false)
            @RequestParam(required = false) Long couponId,

            @ApiParam(name="authorUserId",value = "优惠券主人userId",required = false)
            @RequestParam(required = false) Long authorUserId,

            @NotBlank(message = "请选择优惠券类型")
            @Pattern(regexp = "^(0004-1|0004-2)$",message = "优惠券类型不正确")
            @ApiParam(name="type",value = "优惠券类型,0004-1表示抵用券,0004-2表示折扣券",required = true)
            @RequestParam(required = true) String type,

            @ApiParam(name="discount",value = "折扣值",required = false)
            @RequestParam(required = false) BigDecimal discount,

            @ApiParam(name="offsetMoney",value = "满M减N中的N数值",required = false)
            @RequestParam(required = false) BigDecimal offsetMoney,

            @ApiParam(name="requireMoney",value = "满M减N中的M数值",required = false)
            @RequestParam(required = false) BigDecimal requireMoney,

            @ApiParam(name="startDate",value = "开始时间",required = true)
            @RequestParam(required = true) LocalDate startDate,

            @ApiParam(name="expireDate",value = "过期时间",required = true)
            @RequestParam(required = true) LocalDate expireDate){

        BigDecimal maxValue = new BigDecimal(9999999999L);
        if("0004-1".equals(type)){
            //抵用券
            if(offsetMoney == null || requireMoney == null){
                return ResponseResult.error("门槛金额或者抵用金额不能为空");
            }
            if(offsetMoney.compareTo(maxValue) > 0 || requireMoney.compareTo(maxValue) > 0){
                return ResponseResult.error("门槛金额或者抵用金额最大不能超过9999999999");
            }
            discount = null;
        }else{
            //折扣券
            if(requireMoney == null || discount == null){
                return ResponseResult.error("门槛金额或者折扣值不能为空");
            }
            if(discount.compareTo(new BigDecimal(9.99)) > 0){
                return ResponseResult.error("折扣值不能超过9.99");
            }
            if(requireMoney.compareTo(maxValue) > 0){
                return ResponseResult.error("门槛金额最大不能超过9999999999");
            }
            offsetMoney = null;
        }

        String tokenString = CommonUtil.getTokenString();
        UserToken userToken = tokenManager.get(tokenString);

        CouponEntity couponEntity = null;
        if(couponId == null){
            couponEntity = new CouponEntity();
            couponEntity.setCouponId(IdUtil.genCouponId());
            couponEntity.setCreateUserId(userToken.getUserId());
        }else{
            couponEntity = couponService.getById(couponId);
            if(couponEntity == null){
                return ResponseResult.error("该优惠券不存在");
            }
            if(couponEntity.getType().equals("0005-3")){
                return ResponseResult.error("该优惠券已被使用,禁止修改");
            }
            if(startDate == null || expireDate == null){
                return ResponseResult.error("开始时间和结束时间不能为空");
            }
            if(startDate.isBefore(LocalDate.now())){
                return ResponseResult.error("开始时间不能早于今天");
            }
            if(expireDate.isBefore(startDate.plusDays(1))){
                return ResponseResult.error("过期时间不能早于开始时间之后一天");
            }
        }
        if(authorUserId != null){
            couponEntity.setAuthorUserId(authorUserId);
            couponEntity.setDistributeUserId(userToken.getUserId());
            //未分配
            couponEntity.setStatus("0005-2");
        }else{
            couponEntity.setStatus("0005-1");
        }
        couponEntity.setDiscount(discount);
        couponEntity.setOffsetMoney(offsetMoney);
        couponEntity.setRequireMoney(requireMoney);
        couponEntity.setType(type);
        couponEntity.setStartDate(startDate);
        couponEntity.setExpireDate(expireDate);

        if(couponId == null){
            boolean save = couponService.save(couponEntity);
            return save ? ResponseResult.success() : ResponseResult.error("插入失败");
        }else{
            boolean update = couponService.update(couponEntity, new UpdateWrapper<CouponEntity>()
                    .lambda()
                    .set(CouponEntity::getDiscount, discount)
                    .set(CouponEntity::getOffsetMoney, offsetMoney)
                    .set(CouponEntity::getAuthorUserId, authorUserId)
                    .eq(CouponEntity::getCouponId, couponId));
            return update ? ResponseResult.success() : ResponseResult.error("更新失败");
        }
    }

    @ApiOperation(value = "优惠券列表" ,httpMethod = "GET")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ResponseResult list(

            @ApiParam(name="pageNo",value = "当前页码",required = true)
            @RequestParam(required = true) int pageNo,

            @ApiParam(name="pageSize",value = "每页行数",required = true)
            @RequestParam(required = true) int pageSize,

            @ApiParam(name="authorUserId",value = "优惠券主人userId(管理员)",required = false)
            @RequestParam(required = false) Long authorUserId,

            @Pattern(regexp = "^(0005-1|0005-2|0005-3|0005-4)$",message = "优惠券状态不正确")
            @ApiParam(name="status",value = "优惠券状态,0005-1未分配,0005-2未使用,0005-3已使用,0005-4已禁用",required = false)
            @RequestParam(required = false) Long status,

            @Pattern(regexp = "^(0004-1|0004-2)$",message = "优惠券类型不正确")
            @ApiParam(name="type",value = "优惠券类型,0004-1表示抵用券,0004-2表示折扣券",required = true)
            @RequestParam(required = false) String type,

            @ApiParam(name="discount",value = "起始折扣值",required = false)
            @RequestParam(required = false) BigDecimal startDiscount,

            @ApiParam(name="discount",value = "结束折扣值",required = false)
            @RequestParam(required = false) BigDecimal endDiscount,

            @ApiParam(name="startRequireMoney",value = "起始折扣值",required = false)
            @RequestParam(required = false) BigDecimal startRequireMoney,

            @ApiParam(name="endRequireMoney",value = "结束折扣值",required = false)
            @RequestParam(required = false) BigDecimal endRequireMoney,

            @ApiParam(name="startOffsetMoney",value = "起始优惠金额",required = false)
            @RequestParam(required = false) BigDecimal startOffsetMoney,

            @ApiParam(name="endOffsetMoney",value = "结束优惠金额",required = false)
            @RequestParam(required = false) BigDecimal endOffsetMoney){

        Page<CouponEntity> page = new Page<>();
        page.setSize(pageNo);
        page.setCurrent(pageSize);
        UserToken userToken = tokenManager.get();
        LambdaQueryWrapper<CouponEntity> lambda = new QueryWrapper<CouponEntity>().lambda();
        if(status != null){
            lambda.eq(CouponEntity::getStatus,status);
        }
        if(status != null){
            lambda.eq(CouponEntity::getType,type);
        }
        if(startDiscount != null){
            lambda.gt(CouponEntity::getDiscount,startDiscount);
        }
        if(endDiscount != null){
            lambda.lt(CouponEntity::getDiscount,endDiscount);
        }
        if(startRequireMoney != null){
            lambda.gt(CouponEntity::getRequireMoney,startRequireMoney);
        }
        if(endRequireMoney != null){
            lambda.lt(CouponEntity::getRequireMoney,endRequireMoney);
        }
        if(startOffsetMoney != null){
            lambda.gt(CouponEntity::getOffsetMoney,startOffsetMoney);
        }
        if(endOffsetMoney != null){
            lambda.lt(CouponEntity::getOffsetMoney,endOffsetMoney);
        }

        if(userToken.isAdmin()){
            if(authorUserId != null){
                lambda.eq(CouponEntity::getAuthorUserId,authorUserId);
            }
        }
        lambda.select(CouponEntity::getCouponId);
        int count = couponService.count(lambda);

        lambda.select(
                CouponEntity::getStatus,
                CouponEntity::getType,
                CouponEntity::getDiscount,
                CouponEntity::getOffsetMoney,
                CouponEntity::getRequireMoney,
                CouponEntity::getAuthorUserId);

        couponService.page(page, lambda);
        page.setTotal(count);
        return ResponseResult.success(page);

    }

    @ApiOperation(value = "优惠券详情" ,httpMethod = "GET")
    @RequestMapping(value = "/get/{couponId}",method = RequestMethod.GET)
    public ResponseResult get(
            @NotBlank(message = "优惠券id不能为空")
            @ApiParam(name="couponId",value = "优惠券id",required = true)
            @PathVariable(required = true) Long couponId){

        CouponEntity couponEntity = couponService.getById(couponId);
        return couponEntity != null ? ResponseResult.success(couponEntity) : ResponseResult.error("该优惠券不存在");
    }

    @ApiOperation(value = "删除优惠券" ,httpMethod = "DELETE")
    @RequestMapping(value = "/delete/{couponId}",method = RequestMethod.DELETE)
    public ResponseResult delete(
            @NotBlank(message = "优惠券id不能为空")
            @ApiParam(name="couponId",value = "优惠券id",required = true)
            @PathVariable(required = true) Long couponId){

        return couponService.delete(couponId);
    }

    @ApiOperation(value = "禁用优惠券" ,httpMethod = "PUT")
    @RequestMapping(value = "/disable/{couponId}",method = RequestMethod.PUT)
    public ResponseResult disable(
            @NotBlank(message = "优惠券id不能为空")
            @ApiParam(name="couponId",value = "优惠券id",required = true)
            @PathVariable(required = true) Long couponId){

        return couponService.disableCoupon(couponId);
    }

    @ApiOperation(value = "发放优惠券" ,httpMethod = "PUT")
    @RequestMapping(value = "/distribute/{couponId}/user/{userId}",method = RequestMethod.PUT)
    public ResponseResult distribute(
            @NotBlank(message = "优惠券id不能为空")
            @ApiParam(name="couponId",value = "优惠券id",required = true)
            @PathVariable(required = true) Long couponId,

            @NotBlank(message = "用户id不能为空")
            @ApiParam(name="userId",value = "获得优惠券的用户id",required = true)
            @PathVariable(required = true) Long userId){

        return couponService.distributeDeleteIfExpire(couponId,userId);
    }

}
