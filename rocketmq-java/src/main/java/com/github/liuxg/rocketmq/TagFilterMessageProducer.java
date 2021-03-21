package com.github.liuxg.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.List;

/**
 * @author T-liuxg
 * @ClassName TagFilterMessageProducer
 * @description 根据tag来分类消息
 * @date 2021/3/19 16:11
 */
public class TagFilterMessageProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("TagFilterProduceGroup");
        producer.setNamesrvAddr("10.129.48.71:9876;10.114.55.119:9876");
        producer.start();
        for (int i = 1; i < 3; i++) {
            Message message = new Message("TagFilterTopic","TagFilter-"+i,("hello rocket tagFilter-"+i+" message").getBytes());
            SendResult sendResult = producer.send(message);
            System.out.println(sendResult);
        }
//        producer.shutdown();
    }

}
