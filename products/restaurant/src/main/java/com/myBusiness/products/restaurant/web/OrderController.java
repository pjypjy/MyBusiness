package com.myBusiness.products.restaurant.web;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.common.exception.CouponException;
import com.myBusiness.common.exception.GoodsInfoExpireException;
import com.myBusiness.common.exception.LackOfStockException;
import com.myBusiness.common.exception.MyMenuNotFoundException;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.config.token.TokenManager;
import com.myBusiness.products.restaurant.config.token.UserToken;
import com.myBusiness.products.restaurant.entity.OrderCancelEntity;
import com.myBusiness.products.restaurant.entity.OrderEntity;
import com.myBusiness.products.restaurant.service.OrderService;
import com.myBusiness.products.restaurant.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pjy
 * @since 2018-10-22
 */
@Api(description = "订单管理")
@Controller
@RequestMapping("/order")
@Validated
public class OrderController {

    private final static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @Autowired
    TokenManager tokenManager;

    @ApiOperation(value = "新增 订单" ,httpMethod = "POST")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseResult add(
            @ApiParam(name="couponId",value = "优惠券id",required = false)
            @RequestParam(required = false) String couponId,

            @ApiParam(name="index",value = "我的菜单index",required = true)
            @RequestParam(required = true) Integer index,

            @ApiParam(name="orderRemark",value = "订单备注",required = false)
            @RequestParam(required = false) String orderRemark,

            @ApiParam(name="customerCount",value = "顾客数量",required = true)
            @RequestParam(required = true) Integer customerCount){

        OrderEntity orderEntity = null;
        try {
            orderEntity = orderService.generateOrderFromMyMenu(index, couponId, orderRemark, customerCount);
        } catch (GoodsInfoExpireException e) {
            return ResponseResult.error("商品信息已过期,请重新保存菜单");
        } catch (LackOfStockException e) {
            return ResponseResult.error(e.getGoodsName() + " 库存不足");
        } catch (CouponException e) {
            return ResponseResult.error("优惠券状态错误");
        } catch (MyMenuNotFoundException e) {
            return ResponseResult.error("该菜单不存在");
        }
        return ResponseResult.success(orderEntity.getOrderId());
    }

    @ApiOperation(value = "查询订单列表" ,httpMethod = "GET")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ResponseResult list(
            @ApiParam(name="pageNo",value = "当前页码",required = true)
            @RequestParam(required = true) int pageNo,

            @ApiParam(name="pageSize",value = "每页行数",required = true)
            @RequestParam(required = true) int pageSize,

            @ApiParam(name="startDate",value = "开始时间",required = false)
            @RequestParam(required = false) LocalDateTime startDate,

            @ApiParam(name="endDate",value = "结束时间",required = false)
            @RequestParam(required = false) LocalDateTime endDate,

            @ApiParam(name="startMoney",value = "起始金额",required = false)
            @RequestParam(required = false) BigDecimal startMoney,

            @ApiParam(name="endMoney",value = "结束金额",required = false)
            @RequestParam(required = false) BigDecimal endMoney,

            @ApiParam(name="userId",value = "订单用户id(管理员)",required = false)
            @RequestParam(required = false) Long userId,

            @ApiParam(name="status",value = "订单状态",required = false)
            @RequestParam(required = false) String status,

            @ApiParam(name="cancel",value = "是否取消",required = false)
            @RequestParam(required = false) Boolean cancel){

        Page<OrderEntity> page = new Page<>();
        page.setSize(pageNo);
        page.setCurrent(pageSize);
        UserToken userToken = tokenManager.get();
        LambdaQueryWrapper<OrderEntity> lambda = new QueryWrapper<OrderEntity>().lambda();
        if(startDate != null){
            lambda.gt(OrderEntity::getCreateDate,startDate);
        }
        if(endDate != null){
            lambda.lt(OrderEntity::getCreateDate,endDate);
        }
        if(startMoney != null){
            lambda.gt(OrderEntity::getNeedPayMoney,startMoney);
        }
        if(endMoney != null){
            lambda.lt(OrderEntity::getNeedPayMoney,endMoney);
        }
        if(userId != null){
            lambda.eq(OrderEntity::getUserId,userId);
        }
        if(status != null){
            lambda.eq(OrderEntity::getStatus,status);
        }
        if(cancel != null){
            lambda.isNotNull(OrderEntity::getCancelId);
        }
        if(userToken.isAdmin()){
            if(userId != null){
                lambda.eq(OrderEntity::getUserId,userId);
            }
        }
        lambda.select(OrderEntity::getOrderId);
        int count = orderService.count(lambda);
        lambda.select(
                OrderEntity::getCreateDate,
                OrderEntity::getOriginalMoney,
                OrderEntity::getNeedPayMoney,
                OrderEntity::getStatus,
                OrderEntity::getStatus);
        orderService.page(page, lambda);
        page.setTotal(count);
        return ResponseResult.success(page);
    }

    @ApiOperation(value = "查询订单详情" ,httpMethod = "GET")
    @RequestMapping(value = "/get/{orderId}",method = RequestMethod.GET)
    public ResponseResult get(
            @ApiParam(name="orderId",value = "订单id",required = true)
            @RequestParam(required = true) Long orderId){

        OrderEntity orderEntity = orderService.getById(orderId);
        if(orderEntity != null){
            return ResponseResult.success(orderEntity);
        }
        return ResponseResult.error("该订单不存在");
    }

    @ApiOperation(value = "取消订单" ,httpMethod = "PUT")
    @RequestMapping(value = "/cancel/{orderId}",method = RequestMethod.PUT)
    public ResponseResult cancel(
            @ApiParam(name="orderId",value = "订单id",required = true)
            @PathVariable(required = true) Long orderId,

            @Pattern(regexp = "^(0009-1|0009-2|0009-99)$",message = "取消原因不正确")
            @ApiParam(name="cancelReason",value = "取消原因",required = true)
            @RequestParam(required = true) String cancelReason,

            @ApiParam(name="cancelRemark",value = "备注",required = false)
            @RequestParam(required = false) String cancelRemark){

        if("0009-99".equals(cancelReason) && CommonUtil.isEmpty(cancelRemark)){
            return ResponseResult.error("请在备注填写取消原因");
        }
        OrderEntity orderEntity = orderService.getById(orderId);
        if(orderEntity == null){
            return ResponseResult.error("该订单不存在");
        }
        String status = orderEntity.getStatus();
        switch (status) {
            case "0002-1":
                //已提交到后台 直接标即可
                OrderCancelEntity orderCancelEntity = null;
                try {
                    orderCancelEntity = orderService.cancelOrder(orderId, cancelReason, cancelRemark);
                } catch (Exception e) {
                    logger.error("取消失败", e);
                    return ResponseResult.error("取消订单失败");
                }
                return ResponseResult.success(orderCancelEntity);
            case "0002-2":
                //退款
                return ResponseResult.error("该订单已支付,不能取消");
            case "0002-3":
                return ResponseResult.error("该订单已完成,不能取消");
            case "0002-4":
                return ResponseResult.error("该订单正在取消中...");
            case "0002-5":
                return ResponseResult.error("该订单已被取消...");
            case "0002-6":
                return ResponseResult.error("该订单已被超时关闭...");
            default:
                return ResponseResult.error("订单状态错误");
        }
    }
}
