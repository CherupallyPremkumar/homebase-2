package com.homebase.ecom.catalog.scheduler.config;


import org.quartz.*;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail refreshCollectionsJobDetail() {
        return JobBuilder.newJob(RefreshCollectionsQuartzJob.class)
                .withIdentity("refreshCollectionsJobDetail")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger refreshCollectionsJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(refreshCollectionsJobDetail())
                .withIdentity("refreshCollectionsJobTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInHours(1)
                        .repeatForever())
                .build();
    }

    // The Bridge between Quartz and Spring Batch
    public static class RefreshCollectionsQuartzJob extends QuartzJobBean {
        @Autowired
        private JobOperator jobOperator;
        @Autowired
        private Job refreshCollectionsJob;

        @Override
        protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
            try {
                String paramsString = "time=" + System.currentTimeMillis();
                jobOperator.start(refreshCollectionsJob, paramsString);
            } catch (Exception e) {
                throw new JobExecutionException("Failed to run Batch Job", e);
            }
        }
    }
}
