package com.ch.train.timer.job;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * author: ch
 * create: 2024--1113:24
 * Description:
 */
@Component
@EnableScheduling
public class TestJob {

//    @Scheduled(cron = "0/10 * * * * ?")
    public void test1() {
        System.out.println("test1");
    }
}
