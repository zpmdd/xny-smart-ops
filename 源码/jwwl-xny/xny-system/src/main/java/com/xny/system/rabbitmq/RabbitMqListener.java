//package com.xny.system.rabbitmq;
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RabbitMqListener {
//    @RabbitListener(queues = "simple.queue")
//    public void listenSimplateQueueMessage(String msg){
//        System.out.println("spring消费者接收到消息:"+msg);
//    }
//}
