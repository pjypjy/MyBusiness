package com.myBusiness.products.restaurant.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 将需要关闭的订单放入 ORDER_CLOSE_WAITING_QUEUE 队列
 * ORDER_CLOSE_WAITING_QUEUE 队列中超时，将消息放入 ORDER_CLOSE_WAITING_END_QUEUE(结束等待队列)
 * 监听 ORDER_CLOSE_WAITING_END_QUEUE 队列来处理超时订单
 */
@Configuration
public class RabbitConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";

    public static final String ORDER_CLOSE_WAITING_QUEUE = "order.close.waiting.queue";

    public static final String ORDER_CLOSE_WAITING_BINDING_KEY = "order.close.waiting";

    public static final String ORDER_CLOSE_WAITING_END_QUEUE = "order.close.waitingEnd.queue";

    public static final String ORDER_CLOSE_WAIT_END_BINDING_KEY = "order.close.waitingEnd";

    @Bean
    public Queue orderCloseWaitingQueue() {
        Map<String, Object> args = new HashMap<>(2);
//       x-dead-letter-exchange    声明  死信交换机
        args.put("x-dead-letter-exchange",ORDER_EXCHANGE);
//       x-dead-letter-routing-key    声明 死信路由键
        args.put("x-dead-letter-routing-key",ORDER_CLOSE_WAIT_END_BINDING_KEY);
        return new Queue(ORDER_CLOSE_WAITING_QUEUE,true,false,false,args);
    }

    @Bean
    public Queue orderCloseWaitEndQueue() {
        return new Queue(ORDER_CLOSE_WAITING_END_QUEUE);
    }

    @Bean
    public DirectExchange orderDirectExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding orderCloseWaitingBinding() {
        return BindingBuilder.bind(orderCloseWaitingQueue()).to(orderDirectExchange()).with(ORDER_CLOSE_WAITING_BINDING_KEY);
    }

    @Bean
    public Binding orderCloseWaitEndBinding() {
        return BindingBuilder.bind(orderCloseWaitEndQueue()).to(orderDirectExchange()).with(ORDER_CLOSE_WAIT_END_BINDING_KEY);
    }


    @Bean(name = "orderCloseMessagePostProcessor")
    public MessagePostProcessor orderCloseMessagePostProcessor() {
        return message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            //      设置编码
            messageProperties.setContentEncoding("utf-8");
            //      设置过期时间 X 毫秒
            messageProperties.setExpiration(String.valueOf(60 * 1000 * 60));
            return message;
        };

    }
}
