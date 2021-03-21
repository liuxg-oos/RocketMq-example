package com.github.liuxg.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Mrs.Liuxg
 * @Description: TODO
 * @date 2021/3/2123:11
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup="spring-boot-producer-consumer",topic = "springbootTopic")
public class Consumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        log.info("Recevie message : [{}]",s);
    }
}
