package com.github.liuxg.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * @author T-liuxg
 * @ClassName BroadcastConsumer
 * @description 广播模式消费消息
 * @date 2021/3/19 14:01
 */
public class BroadcastConsumer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("BroadcastConsumerGroup");
        consumer.setNamesrvAddr("10.129.48.71:9876;10.114.55.119:9876");
        consumer.subscribe("TopicTest","*");
        consumer.setMessageModel(MessageModel.BROADCASTING);//consumer广播模式订阅TopicTest主题
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.printf("%s Received message: %s%n",Thread.currentThread(),msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("BroadcastConsumer started.");
    }

}
