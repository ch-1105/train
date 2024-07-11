package com.ch.timer.config;

import com.ch.timer.job.QuartzJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author: ch
 * create: 2024--1113:36
 * Description:
 */
@Configuration
public class QuartzConfig {
    /**
     * 声明一个任务
     * @return
     */
     @Bean
     public JobDetail jobDetail() {
         return JobBuilder.newJob(QuartzJob.class)
                 .withIdentity("QuartzJob", "test")
                 .storeDurably()
                 .build();
     }

     /**
      * 声明一个触发器，什么时候触发这个任务
      * @return
      */
     @Bean
     public Trigger trigger() {
         return TriggerBuilder.newTrigger()
                 .forJob(jobDetail())
                 .withIdentity("trigger", "trigger")
                 .startNow()
                 .withSchedule(CronScheduleBuilder.cronSchedule("*/20 * * * * ?"))
                 .build();
     }
}
