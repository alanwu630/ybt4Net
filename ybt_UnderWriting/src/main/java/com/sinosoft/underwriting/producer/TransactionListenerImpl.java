package com.sinosoft.underwriting.producer;

import com.sinosoft.underwriting.service.ConfirmService;
import com.sinosoft.underwriting.service.impl.ConfirmServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

@Slf4j
@RocketMQTransactionListener
public class TransactionListenerImpl implements RocketMQLocalTransactionListener {

    @Autowired
    private ConfirmServiceImpl confirmService;

    /**
     * 执行业务逻辑
     * @param message
     * @param o
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        // 获取消息体里参数
        MessageHeaders messageHeaders = message.getHeaders();
        String transactionId = (String) messageHeaders.get(RocketMQHeaders.TRANSACTION_ID);
        log.info("【执行本地事务】消息体参数：transactionId={}", transactionId);


        try {
            if (!confirmService.updatePolicyConfirmFlag(transactionId)) {
                log.error("【执行本地事务】插入数据库失败，消息将被回滚");
                return RocketMQLocalTransactionState.ROLLBACK;
            }
            return RocketMQLocalTransactionState.COMMIT; // 正常：向MQ Server发送commit消息
        } catch (Exception e) {
            log.error("【执行本地事务】发生异常，消息将被回滚", e);
            return RocketMQLocalTransactionState.ROLLBACK; // 异常：向MQ Server发送rollback消息
        }
    }

    /**
     * 回调
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        // 获取消息体里参数
        MessageHeaders messageHeaders = message.getHeaders();
        String transactionId = (String) messageHeaders.get(RocketMQHeaders.TRANSACTION_ID);
        log.info("Broker回调方法->【执行本地事务】消息体参数：transactionId={}", transactionId);

        try {
            Integer falg = confirmService.selectFlagByProposalNo(transactionId);
            if (falg != 1 ) {
                log.error("Broker回调方法->【执行本地事务】插入数据库失败，消息将被回滚");
                return RocketMQLocalTransactionState.ROLLBACK;
            }
            return RocketMQLocalTransactionState.COMMIT; // 正常：向MQ Server发送commit消息
        } catch (Exception e) {
            log.error("Broker回调方法->【执行本地事务】发生异常，消息将被回滚", e);
            return RocketMQLocalTransactionState.ROLLBACK; // 异常：向MQ Server发送rollback消息
        }
    }
}
