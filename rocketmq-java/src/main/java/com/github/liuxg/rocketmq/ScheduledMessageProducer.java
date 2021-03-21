package com.github.liuxg.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * @author T-liuxg
 * @ClassName ScheduledMessageProducer
 * @description 延时消息生产者,比如新建一个订单但还未付款有30分钟的有效期
 * @date 2021/3/19 15:23
 */
public class ScheduledMessageProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ScheduledProducerGroup");
        producer.setNamesrvAddr("10.129.48.71:9876;10129.48.71:9876");
        producer.start();
        for (int i = 0; i < 5; i++) {
            Message message = new Message("ScheduleTopic", ("Hello scheduled message " + i).getBytes());
            message.setDelayTimeLevel(4);//休眠10s
            //private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    System.out.println(e);
                }
            });
        }
//        producer.shutdown();
    }

}
