package com.ch.timer.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * author: ch
 * create: 2024--1113:33
 * Description:
 * @author ch051
 */
@DisallowConcurrentExecution // 禁止并发执行
public class QuartzJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(Thread.currentThread().getName() + "执行任务");
    }
}
