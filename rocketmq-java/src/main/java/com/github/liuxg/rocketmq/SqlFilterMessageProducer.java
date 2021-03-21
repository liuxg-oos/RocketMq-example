package com.github.liuxg.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author T-liuxg
 * @ClassName SqlFilterMessageProducer
 * @description 根据表达式来过滤消息
 * @date 2021/3/19 16:22
 */
public class SqlFilterMessageProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("SqlFilterProducerGroup");
        producer.setNamesrvAddr("10.129.48.71:9876;10.114.55.119:9876");
        producer.start();
        List<Message> msgs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message msg = new Message("SqlFilterTopic","SqlFilterTag",("hello sql rocket mq"+i).getBytes());
            msg.putUserProperty("id",String.valueOf(i));
            msgs.add(msg);
        }
        producer.send(msgs);
    }
}
