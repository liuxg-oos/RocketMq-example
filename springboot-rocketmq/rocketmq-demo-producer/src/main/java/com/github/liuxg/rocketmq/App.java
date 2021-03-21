package com.github.liuxg.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@RestController
@Slf4j
public class App 
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class,args);
    }

    @Autowired
    private RocketMQTemplate template;

    @GetMapping("/hello")
    public String hello() {
        template.convertAndSend("springbootTopic","hello world springboot rocketmq");
        log.info("hello");
        return "hello";
    }
}
