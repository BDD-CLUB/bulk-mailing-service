package io.springbatch.springbatch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class MonthMailingService {

    private final ApplicationContext context;
    private final JobLauncher jobLauncher;

    public void runMailingBatch(String jobName) throws Exception {
        final Job findJob = context.getBean(jobName, Job.class);

        // 실제 운영에서는 startTime을 받을 것.
        // 테스트에서는 LocalDate.now를 사용해서 현재 시간을 계속 받도록 함.
        Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        JobParameters jobParameter = new JobParametersBuilder()
                .addDate("startTime", date)
                .toJobParameters();

        this.jobLauncher.run(findJob, jobParameter);
    }

}
