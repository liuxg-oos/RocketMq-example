package com.github.liuxg.rocketmq;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Mrs.Liuxg
 * @Description: TODO
 * @date 2021/3/2122:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class})
public class ProducerTest {

    @Autowired
    private RocketMQTemplate template;

    @Test
    public void test() {
        template.convertAndSend("springbootTopic","hello springboot rocketmq");
    }

}
