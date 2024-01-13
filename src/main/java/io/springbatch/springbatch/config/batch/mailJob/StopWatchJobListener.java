package io.springbatch.springbatch.config.batch.mailJob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StopWatchJobListener implements JobExecutionListener {

    @Override
    public void afterJob(JobExecution jobExecution) {
        int time = jobExecution.getEndTime().getSecond() - jobExecution.getStartTime().getSecond();

        log.info("Spring Batch Mailing Service Time = {}", time);
    }
}
