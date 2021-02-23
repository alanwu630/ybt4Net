package com.sinosoft.underwriting.producer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PolicyProducer {

    @Autowired
    private RocketMQTemplate t;

    public void send(String Topic ,String message){


        t.convertAndSend(Topic,message);
//        t.asyncSend(Topic, message, new SendCallback() {
//            @Override
//            public void onSuccess(SendResult sendResult) {
//                System.out.println("发送成功");
//            }
//
//            @Override
//            public void onException(Throwable throwable) {
//                System.out.println("发送失败");
//            }
//        });

        //发送顺序消息
//        t.syncSendOrderly("Topic1", "98456237,创建", "98456237");
//        t.syncSendOrderly("Topic1", "98456237,支付", "98456237");
//        t.syncSendOrderly("Topic1", "98456237,完成", "98456237");
    }
}
