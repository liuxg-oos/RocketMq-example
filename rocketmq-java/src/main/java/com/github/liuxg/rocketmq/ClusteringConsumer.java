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
 * @ClassName ClusteringConsumer
 * @description 服务器主动推送消息，客户端采用负载均衡的方式获取消息
 * @date 2021/3/19 13:48
 */
public class ClusteringConsumer {

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ClusteringConsumerGroup");
        consumer.setNamesrvAddr("10.114.55.119:9876;10.129.48.71:9876");
        consumer.subscribe("TopicTest","*");//订阅TopicTest主题下所有的消息
        consumer.setMessageModel(MessageModel.CLUSTERING);//集群方式消费消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.printf("%s Received New message: %s%n",Thread.currentThread(),msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("Consumer started.");
    }

}
