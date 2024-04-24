package com.ch.member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class MemberApplication {
    static Logger logger = LoggerFactory.getLogger(MemberApplication.class);
    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(MemberApplication.class, args);
        logger.info("启动成功");
        logger.info("端口号：{}",run.getEnvironment().getProperty("server.port"));
    }

}
