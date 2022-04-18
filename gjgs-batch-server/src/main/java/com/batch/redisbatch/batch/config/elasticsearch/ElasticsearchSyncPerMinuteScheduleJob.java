package com.batch.redisbatch.batch.config.elasticsearch;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ElasticsearchSyncPerMinuteScheduleJob extends QuartzJobBean {

    private final Job elasticsearchSyncPerMinuteJob;
    private final JobExplorer jobExplorer;
    private final JobLauncher jobLauncher;

    public ElasticsearchSyncPerMinuteScheduleJob(@Qualifier("elasticsearchSyncPerMinuteJob") Job elasticsearchSyncPerMinuteJob, JobExplorer jobExplorer, JobLauncher jobLauncher) {
        this.elasticsearchSyncPerMinuteJob = elasticsearchSyncPerMinuteJob;
        this.jobExplorer = jobExplorer;
        this.jobLauncher = jobLauncher;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer)
                .getNextJobParameters(this.elasticsearchSyncPerMinuteJob)
                .toJobParameters();
        try {
            this.jobLauncher.run(elasticsearchSyncPerMinuteJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
