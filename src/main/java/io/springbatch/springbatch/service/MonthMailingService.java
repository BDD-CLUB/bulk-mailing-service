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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MonthMailingService {

    private final ApplicationContext context;
    private final JobLauncher jobLauncher;

    @Value("${mailing.password}")
    private String configPassword;

    public void runMailingBatch(String jobName, String subject, String reportMessage, String password) throws Exception {
        if (!Objects.equals(password, configPassword)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        final Job findJob = context.getBean(jobName, Job.class);

        Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        JobParameters jobParameter = new JobParametersBuilder()
                .addDate("startTime", date)
                .addString("randomStringForTest", generateRandomString()) // 실제 운영에서는 삭제할 것.
                .addString("mailSubject", subject)
                .addString("mailMessage", reportMessage)
                .toJobParameters();

        this.jobLauncher.run(findJob, jobParameter);
    }

    private String generateRandomString() {
        int leftLimit = 97; // letter 'a'4
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
