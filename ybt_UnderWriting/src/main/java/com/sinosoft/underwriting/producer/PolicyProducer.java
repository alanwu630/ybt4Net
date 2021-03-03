package com.sinosoft.underwriting.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PolicyProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

//    @Value("${MqTxProducerGroup}")
//    private String mqTxProducerGroup;

    /**
     * 发送事务消息
     * @param Topic
     * @param message
     */
    public void sendTxMessage(String Topic ,String message){
        log.info("发送事务异步消息，Message："+message);
        //构建Message信息
        Message msg = MessageBuilder.withPayload(message).build();
        //发送事务消息，（参数1，主题Topic；参数2，消息本体;消息id（暂无要求））
        TransactionSendResult result = rocketMQTemplate.sendMessageInTransaction(Topic, msg, message);
        String sendStatus = result.getSendStatus().name();
        String localTXState = result.getLocalTransactionState().name();
        log.info(">>>> send status={},localTransactionState={} <<<<",sendStatus,localTXState);

        log.info("发送事务消息结束~");
    }

    /**
     * 发送普通消息
     * @param Topic
     * @param message
     */
    public void send(String Topic ,String message){


        rocketMQTemplate.convertAndSend(Topic,message);
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
