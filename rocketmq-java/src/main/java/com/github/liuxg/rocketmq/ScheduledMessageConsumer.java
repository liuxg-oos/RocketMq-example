package com.github.liuxg.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * @author T-liuxg
 * @ClassName ScheduledMessageConsumer
 * @description
 * @date 2021/3/19 15:31
 */
public class ScheduledMessageConsumer {

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ScheduledConsumerGroup");
        consumer.setNamesrvAddr("10.129.48.71:9876;10129.48.71:9876");
        consumer.subscribe("ScheduleTopic","*");
        consumer.setMessageModel(MessageModel.CLUSTERING);//负载的方式消费消息
        long nowTime = System.currentTimeMillis();
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                msgs.forEach(messageExt -> {
                    System.out.printf("%s cost: [%d] received message: [%s]%n",Thread.currentThread().getName(),(System.currentTimeMillis()-nowTime),messageExt);
                });
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }

}
