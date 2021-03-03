package com.sinosoft.newbiz.consumer;

import com.sinosoft.common.RedisUtil;
import com.sinosoft.newbiz.consumer.service.NewbizService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "Topic1", consumerGroup = "consumer-policy")
public class PolicyConsumer implements RocketMQListener<String> {

    @Autowired
    private NewbizService newbizService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onMessage(String msg) {

        log.info(">>>>收到事务消息： "+msg);

        String proposalno = msg;
        //消费者接收到消息之后，将redis中的保单删除
        log.info("在redis中查询"+"TxPolicyCode|"+proposalno+",开始做消费幂等性校验");
        Object o = redisUtil.get("TxPolicyCode|" + proposalno);
        if (null == o) {
            log.info(">>>>保单号： " + msg + "已经被消费过");
        } else {
            newbizService.confirmServiceProcess(proposalno);
        }


    }

}
