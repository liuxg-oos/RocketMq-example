package com.github.liuxg.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author T-liuxg
 * @ClassName OneWayProducer
 * @description 单向发送消息(注意不会等待服务器ack响应)，日志发送
 * @date 2021/3/19 13:41
 */
public class OneWayProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("OnewayProducerGroup");
        producer.setNamesrvAddr("10.114.55.119:9876;10.129.48.71:9876");
        producer.start();
        for (int i = 0; i < 20; i++) {
            Message message = new Message("TopicTest","TagB",("Hello RocketMq"+i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.sendOneway(message);
        }
        producer.shutdown();
    }
}
