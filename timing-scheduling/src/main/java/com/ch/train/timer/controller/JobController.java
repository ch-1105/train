package com.ch.train.timer.controller;

import com.ch.train.timer.req.CronJobReq;
import com.ch.train.timer.resp.CronJobResp;
import com.ch.train.common.result.Result;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "timer/admin/job")
public class JobController {

    private static final Logger log = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @RequestMapping(value = "/run")
    public Result<Object> run(@RequestBody CronJobReq cronJobReq) throws SchedulerException {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        log.info("手动执行任务开始：{}, {}", jobClassName, jobGroupName);
        schedulerFactoryBean.getScheduler().triggerJob(JobKey.jobKey(jobClassName, jobGroupName));
        return Result.success();
    }

    @RequestMapping(value = "/add")
    public Result<String> add(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        String cronExpression = cronJobReq.getCronExpression();
        String description = cronJobReq.getDescription();
        log.info("创建定时任务开始：{}，{}，{}，{}", jobClassName, jobGroupName, cronExpression, description);
        boolean result = true;
        String message = "";
        try {
            // 通过SchedulerFactory获取一个调度器实例
            Scheduler sched = schedulerFactoryBean.getScheduler();

            // 启动调度器
            sched.start();

            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(jobClassName)).withIdentity(jobClassName, jobGroupName).build();

            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName).withDescription(description).withSchedule(scheduleBuilder).build();

            sched.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            log.error("创建定时任务失败:" + e);
            result=false;
            message = "创建定时任务失败:调度异常";
        } catch (ClassNotFoundException e) {
            log.error("创建定时任务失败:" + e);
            result=false;
            message = "创建定时任务失败：任务类不存在";
        }
        log.info("创建定时任务结束：{}", result ? Result.success() : Result.fail(1000, message));
        return result ? Result.success() : Result.fail(1100, message);
    }

    @RequestMapping(value = "/pause")
    public Result<String> pause(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        log.info("暂停定时任务开始：{}，{}", jobClassName, jobGroupName);
        boolean result = true;
        String message = "";
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            log.error("暂停定时任务失败:" + e);
            result = false;
            message = "暂停定时任务失败:调度异常";
        }
        log.info("暂停定时任务结束：{}", result ? Result.success() : Result.fail(1100, message));
        return result ? Result.success() : Result.fail(1100, message);
    }

    @RequestMapping(value = "/resume")
    public Result<String> resume(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        log.info("重启定时任务开始：{}，{}", jobClassName, jobGroupName);
        boolean result = true;
        String message = "";
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            log.error("重启定时任务失败:" + e);
            result = false;
            message="重启定时任务失败:调度异常";
        }
        log.info("重启定时任务结束：{}", result ? Result.success() : Result.fail(1100, message));
        return result ? Result.success() : Result.fail(1100, message);
    }

    @RequestMapping(value = "/reschedule")
    public Result<String> reschedule(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        String cronExpression = cronJobReq.getCronExpression();
        String description = cronJobReq.getDescription();
        log.info("更新定时任务开始：{}，{}，{}，{}", jobClassName, jobGroupName, cronExpression, description);
        boolean result = true;
        String message = "";
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTriggerImpl trigger1 = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
            trigger1.setStartTime(new Date()); // 重新设置开始时间
            CronTrigger trigger = trigger1;

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withDescription(description).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (Exception e) {
            log.error("更新定时任务失败:" + e);
            result = false;
            message="更新定时任务失败:调度异常";
        }
        log.info("更新定时任务结束：{}", result ? Result.success() : Result.fail(1100, message));
        return result ? Result.success() : Result.fail(1100, message);
    }

    @RequestMapping(value = "/delete")
    public Result<String> delete(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        log.info("删除定时任务开始：{}，{}", jobClassName, jobGroupName);
        boolean result = true;
        String message = "";
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            log.error("删除定时任务失败:" + e);
            result = false;
            message="删除定时任务失败:调度异常";
        }
        log.info("删除定时任务结束：{}", result ? Result.success() : Result.fail(1100, message));
        return result ? Result.success() : Result.fail(1100, message);
    }

    @RequestMapping(value="/query")
    public Result<List<CronJobResp>> query() {
        log.info("查看所有定时任务开始");
        boolean result = true;
        String message = "";
        List<CronJobResp> cronJobDtoList = new ArrayList();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    CronJobResp cronJobResp = new CronJobResp();
                    cronJobResp.setName(jobKey.getName());
                    cronJobResp.setGroup(jobKey.getGroup());

                    //get job's trigger
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    CronTrigger cronTrigger = (CronTrigger) triggers.get(0);
                    cronJobResp.setNextFireTime(cronTrigger.getNextFireTime());
                    cronJobResp.setPreFireTime(cronTrigger.getPreviousFireTime());
                    cronJobResp.setCronExpression(cronTrigger.getCronExpression());
                    cronJobResp.setDescription(cronTrigger.getDescription());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
                    cronJobResp.setState(triggerState.name());

                    cronJobDtoList.add(cronJobResp);
                }

            }
        } catch (SchedulerException e) {
            log.error("查看定时任务失败:" + e);
            result = false;
            message="查看定时任务失败:调度异常";
        }
        log.info("查看定时任务结束：{}", result ? Result.success(cronJobDtoList) : Result.fail(1100, message));
        return result ? Result.success(cronJobDtoList) : Result.fail(1100, message);
    }

}
