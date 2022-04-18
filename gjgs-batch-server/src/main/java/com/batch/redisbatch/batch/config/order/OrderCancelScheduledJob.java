package com.batch.redisbatch.batch.config.order;

import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

//@RequiredArgsConstructor
public class OrderCancelScheduledJob extends QuartzJobBean {

    private final Job job;
    private final JobExplorer jobExplorer;
    private final JobLauncher jobLauncher;

    public OrderCancelScheduledJob(@Qualifier("teamOrderCancelJob") Job job, JobExplorer jobExplorer, JobLauncher jobLauncher) {
        this.job = job;
        this.jobExplorer = jobExplorer;
        this.jobLauncher = jobLauncher;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        // 파라미터 추출
        JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer)
                .getNextJobParameters(this.job)
                .toJobParameters();

        // 잡 실행
        try {
            this.jobLauncher.run(this.job, jobParameters);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
