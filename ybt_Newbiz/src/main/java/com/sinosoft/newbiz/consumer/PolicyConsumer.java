package com.sinosoft.newbiz.consumer;

import com.sinosoft.newbiz.consumer.service.NewbizService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "Topic1", consumerGroup = "consumer-policy")
public class PolicyConsumer implements RocketMQListener<String> {

    @Autowired
    private NewbizService newbizService;

    @Override
    public void onMessage(String s) {
        System.out.println("收到： "+s);
        String proposalno = s;
        newbizService.confirmServiceProcess(proposalno);
    }

}
