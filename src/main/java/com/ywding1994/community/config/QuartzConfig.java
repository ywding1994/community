package com.ywding1994.community.config;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.ywding1994.community.quartz.PostScoreRefreshJob;

/**
 * Quartz配置类
 */
@Configuration
public class QuartzConfig {

    /**
     * 配置刷新讨论帖分数任务
     *
     * @return 刷新讨论帖分数任务配置
     */
    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(PostScoreRefreshJob.class);
        jobDetailFactoryBean.setName("postScoreRefreshJob");
        jobDetailFactoryBean.setGroup("communityJobGroup");
        jobDetailFactoryBean.setDurability(true);
        jobDetailFactoryBean.setRequestsRecovery(true);
        return jobDetailFactoryBean;
    }

    /**
     * 配置刷新讨论帖分数任务触发器
     *
     * @param postScoreRefreshJobDetail 刷新讨论帖分数任务配置
     * @return 刷新讨论帖分数任务触发器配置
     */
    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail) {
        SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
        simpleTriggerFactoryBean.setJobDetail(postScoreRefreshJobDetail);
        simpleTriggerFactoryBean.setName("postScoreRefreshTrigger");
        simpleTriggerFactoryBean.setGroup("communityTriggerGroup");
        simpleTriggerFactoryBean.setRepeatInterval(1000 * 60 * 5);
        simpleTriggerFactoryBean.setJobDataMap(new JobDataMap());
        return simpleTriggerFactoryBean;
    }

}
