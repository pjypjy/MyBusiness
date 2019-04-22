package com.myBusiness.products.restaurant.rabbitClient;

import com.myBusiness.products.restaurant.config.rabbit.RabbitConfig;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OrderCloseSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier(value = "orderCloseMessagePostProcessor")
    MessagePostProcessor messagePostProcessor;

    public void send(long orderId) {
        this.rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_EXCHANGE,
                RabbitConfig.ORDER_CLOSE_WAITING_BINDING_KEY,
                orderId,
                messagePostProcessor);
    }


}
