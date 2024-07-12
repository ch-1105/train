package com.ch.train.timer.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ch.train.common.result.Result;
import com.ch.train.timer.feign.BusinessFeign;
import jakarta.annotation.Resource;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * author: ch
 * create: 2024--1214:09
 * Description:
 */
public class DailyTrainJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(DailyTrainJob.class);

    @Resource
    private BusinessFeign businessFeign;

    /**
     * 生成15天后的车次数据
     * @date 2024/7/12 15:42
     * @param jobExecutionContext
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("每日车次数据生成中");
        Date date = new Date();
        DateTime dateTime = DateUtil.offsetDay(date, 15);
        Date jdkDate = dateTime.toJdkDate();
        Result<Object> objectResult = businessFeign.generateDailyTrain(jdkDate);

        log.info("每日车次数据生成结束 结果: {}", objectResult);
    }
}
