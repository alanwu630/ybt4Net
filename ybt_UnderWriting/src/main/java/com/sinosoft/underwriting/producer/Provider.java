package com.sinosoft.underwriting.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class Provider {
    public static void main(String[] args) throws MQClientException, InterruptedException, RemotingException, MQBrokerException {
        //创建一个生产者
        DefaultMQProducer producer=new DefaultMQProducer("rmq-group");
        //设置NameServer地址
        producer.setNamesrvAddr("192.168.26.128:9876");
        //设置生产者实例名称
        producer.setInstanceName("provider");
        //启动生产者
        producer.start();
        //发送消息
        for (int i = 1; i <=10 ; i++) {
//            Thread.sleep(1000); //模拟网络延迟
            //创建消息  topic代表主题名称     tags代表小分类     body代表消息体
            Message message=new Message("weksoft_topic","TagA","fskadfkasdf".getBytes());
            //发送消息
            SendResult send = producer.send(message);
            System.out.println(send.toString());
        }
    }
}
