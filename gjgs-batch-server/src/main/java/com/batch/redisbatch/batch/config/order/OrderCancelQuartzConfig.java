package com.batch.redisbatch.batch.config.order;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderCancelQuartzConfig {
    @Bean
    public JobDetail quartzJobDetail() {
        return JobBuilder.newJob(OrderCancelScheduledJob.class)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(30).repeatForever();
                //.withIntervalInSeconds(20).repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(quartzJobDetail())
                .withSchedule(scheduleBuilder)
                .build();
    }
}
