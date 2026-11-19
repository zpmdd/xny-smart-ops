//package com.xny.system.rabbitmq;
//
//import com.xny.common.utils.R;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
//@Component
//public class RabbitMqPublisher {
//    @Resource
//    private RabbitTemplate rabbitTemplate;
//
//    /**
//     * 发布消息
//     * @param queueName
//     * @param message
//     * @return
//     */
//    public R<String> publish(String queueName,String message) {
//        rabbitTemplate.convertAndSend(queueName,message);
//        return R.ok();
//    }
//}
