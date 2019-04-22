package com.myBusiness.products.restaurant.web;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.myBusiness.products.restaurant.entity.OrderEntity;
import com.myBusiness.products.restaurant.enums.OrderStatus;
import com.myBusiness.products.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/callBack")
public class CallBackController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/weChat")
    public void weChatPayCallBack(){
        /**
         * 校验成功
         */
        String orderId = "";
        orderService.update(new OrderEntity(),
                new UpdateWrapper<OrderEntity>().lambda()
                        .set(OrderEntity::getStatus,OrderStatus.PAID.getCode()));
    }
}
