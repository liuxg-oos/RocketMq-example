package com.github.liuxg.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author T-liuxg
 * @ClassName FIFOConsumer
 * @description 顺序消息消费，带事务方式（应用可控制Offset什么时候提交）
 * @date 2021/3/19 14:30
 */
public class FIFOConsumer {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("FIFOConsumerGroup");
        consumer.setNamesrvAddr("10.129.48.71:9876;10.114.55.119:9876");
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.subscribe("OrderTopic", "OrderIDA || OrderIDB || OrderIDC");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            Random random = new Random();
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                context.setAutoCommit(true);
                for (MessageExt ext : msgs) {
                    System.out.printf("consumer: %s queueId: %d received message: [%s]%n",Thread.currentThread().getName(),ext.getQueueId(),new String(ext.getBody()));
                }
                try {
                    TimeUnit.SECONDS.sleep(random.nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
    }
}
