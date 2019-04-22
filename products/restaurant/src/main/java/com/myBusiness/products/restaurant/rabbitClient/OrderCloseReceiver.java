package com.myBusiness.products.restaurant.rabbitClient;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.myBusiness.products.restaurant.config.rabbit.RabbitConfig;
import com.myBusiness.products.restaurant.entity.OrderEntity;
import com.myBusiness.products.restaurant.enums.OrderStatus;
import com.myBusiness.products.restaurant.service.OrderService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OrderCloseReceiver {

    private final static Logger logger = LoggerFactory.getLogger(OrderCloseReceiver.class);

    @Autowired
    OrderService orderService;

    @RabbitListener(queues = RabbitConfig.ORDER_CLOSE_WAITING_END_QUEUE)
    public void ackConsumer(long orderId, Message message, Channel channel) throws IOException {
        logger.info("receive order close task, orderId:{}",orderId);
        orderService.update(new OrderEntity(),
                new UpdateWrapper<OrderEntity>()
                        .lambda()
                        .set(OrderEntity::getStatus, OrderStatus.COMMITTED.getCode())
                        .eq(OrderEntity::getStatus, OrderStatus.TIMEOUT_CLOSED.getCode())
                        .eq(OrderEntity::getOrderId, orderId));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
