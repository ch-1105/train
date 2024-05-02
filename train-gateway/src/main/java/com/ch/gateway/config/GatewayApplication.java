package com.ch.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = "com.ch")
@SpringBootApplication
public class GatewayApplication {
    static Logger logger = LoggerFactory.getLogger(GatewayApplication.class);
    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(GatewayApplication.class, args);
        logger.info("启动成功");
        logger.info("端口号：{}",run.getEnvironment().getProperty("server.port"));
        //打印Ip+端口号
        logger.info("网关地址：http://127.0.0.1:{}",
                run.getEnvironment().getProperty("server.port")
                );
    }

}
