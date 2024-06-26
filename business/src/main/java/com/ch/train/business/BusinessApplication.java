package com.ch.train.business;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = "com.ch")
@MapperScan(basePackages = "com.ch.train.business.mapper")
@SpringBootApplication
public class BusinessApplication {
    static Logger logger = LoggerFactory.getLogger(BusinessApplication.class);
    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(BusinessApplication.class, args);
        logger.info("business 启动成功");
        logger.info("端口号：{}",run.getEnvironment().getProperty("server.port"));
        //打印Ip+端口号
        logger.info("Ip地址：http://127.0.0.1:{}{}",
                run.getEnvironment().getProperty("server.port"),
                run.getEnvironment().getProperty("server.servlet.context-path"));
    }

}
