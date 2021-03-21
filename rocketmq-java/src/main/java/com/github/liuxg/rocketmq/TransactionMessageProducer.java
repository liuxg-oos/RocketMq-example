package com.github.liuxg.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mrs.Liuxg
 * @Description: 事务检查类
 *              事务消息共有三种状态，提交状态、回滚状态、中间状态：<br/>
 *                  TransactionStatus.CommitTransaction: 提交事务，它允许消费者消费此消息。<br/>
 *                  TransactionStatus.RollbackTransaction: 回滚事务，它代表该消息将被删除，不允许被消费。<br/>
 *                  TransactionStatus.Unknown: 中间状态，它代表需要检查消息队列来确定状态。<br/>
 * @date 2021/3/2121:34
 */
public class TransactionMessageProducer {

    private static AtomicInteger count = new AtomicInteger(1);

    public static void main(String[] args) throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer("TransactionProducerGroup2");
        producer.setNamesrvAddr("192.168.86.135:9876");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));
        producer.setExecutorService(executor);
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                //当半消息发送成功时会回调此方法
                System.out.println("当半消息发送成功后，回调该方法，执行本地事务"+count.incrementAndGet());
                if (message.getTags().equals("TagCommit")) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                } else if (message.getTags().equals("TagRollback")) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                } else if (message.getTags().equals("TagUnknow")){
                    return LocalTransactionState.UNKNOW;
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                //当半消息发送成功时，而RocketMQ服务器长时间未收到半消息的状态时，会回调该方法检查本地事务的状态
                int status = count.incrementAndGet();
                System.out.println("RocketMQ服务器检查该消息["+messageExt+"]的本地事务"+status);
                if (status >= 5) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                return LocalTransactionState.UNKNOW;
            }
        });
        producer.start();
        String[] tags = new String[]{"TagCommit", "TagRollback", "TagUnknow"};
        for (int i = 0; i < 3; i++) {
            try {
                Message msg = new Message("TransactionTopic2", tags[i % tags.length], "KEY" + i,
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.sendMessageInTransaction(msg, null);
                System.out.printf("%s%n", sendResult);
                TimeUnit.SECONDS.sleep(1);
            } catch (MQClientException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

}
