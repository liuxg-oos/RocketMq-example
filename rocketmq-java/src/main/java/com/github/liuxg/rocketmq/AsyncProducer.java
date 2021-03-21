package com.github.liuxg.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

/**
 * @author T-liuxg
 * @ClassName AsyncProducer
 * @description 异步发送消息(非阻塞，但会接收服务器的ack响应)
 * @date 2021/3/18 16:47
 */
public class AsyncProducer {

    public static void main(String[] args) throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("AsyncProducerGroup");
        // 设置NameServer的地址
        producer.setNamesrvAddr("10.114.55.119:9876;10.129.48.71:9876");
        // 启动Producer实例
        producer.start();
        producer.setSendMsgTimeout(5000);
        producer.setRetryTimesWhenSendAsyncFailed(0);
        CountDownLatch latch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            final int index = i;
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TopicTest",
                    "TagA",
                    "OrderID188",
                    ("Hello world"+index).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // SendCallback接收异步返回结果的回调
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    latch.countDown();
                    System.out.printf("%-10d OK %s %n", index,
                            sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    latch.countDown();
                    System.out.printf("%-10d Exception %s %n", index, e);
//                    e.printStackTrace();
                }
            });
        }
        latch.await();
        System.out.println("关闭客户端");
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }

}
