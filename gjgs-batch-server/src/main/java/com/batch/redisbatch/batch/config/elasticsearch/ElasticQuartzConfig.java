package com.batch.redisbatch.batch.config.elasticsearch;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticQuartzConfig {
    @Bean
    public JobDetail elasticsearchSyncPerMinuteQuartzJobDetail() {
        return JobBuilder.newJob(ElasticsearchSyncPerMinuteScheduleJob.class)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger elasticsearchSyncPerMinuteJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(1).repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(elasticsearchSyncPerMinuteQuartzJobDetail())
                .withSchedule(scheduleBuilder)
                .build();
    }
}
