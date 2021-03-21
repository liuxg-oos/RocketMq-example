package com.github.liuxg.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author T-liuxg
 * @ClassName BatchMessageProducer
 * @description 批量发送消息
 * @date 2021/3/19 15:55
 */
public class BatchMessageProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("BatchProducerGroup");
        producer.setNamesrvAddr("10.129.48.71:9876;10.114.55.119");
        producer.start();
        List<Message> msgs = new ArrayList<>(5);
        for(int i = 0; i < 5; i++) {
            Message message = new Message();
            message.setTopic("BatchTopic");
            message.setTags("BatchTag-"+i);
            message.setBody(("batch rokcet mq"+i).getBytes());
            msgs.add(message);
        }
        producer.send(msgs,20000);
    }

}
