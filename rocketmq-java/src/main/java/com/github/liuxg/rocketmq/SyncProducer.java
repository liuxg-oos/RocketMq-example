package com.github.liuxg.rocketmq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @author T-liuxg
 * @ClassName SyncProducer
 * @description 同步阻塞发送消息,短信通知
 * @date 2021/3/18 15:44
 */
public class SyncProducer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("SyncProducerGroups");
        producer.setNamesrvAddr("10.129.48.71:9876");
        producer.start();
        for (int i = 0; i < 10; i++) {
            try {
                Message message = new Message("TopicTest","TagC",("hello world rocketmq"+i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                long now = System.currentTimeMillis();
                SendResult result = producer.send(message);
                System.out.println(System.currentTimeMillis()-now);
                System.out.println(result);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MQClientException e) {
                e.printStackTrace();
            } catch (RemotingException e) {
                e.printStackTrace();
            } catch (MQBrokerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        producer.shutdown();
    }

}
