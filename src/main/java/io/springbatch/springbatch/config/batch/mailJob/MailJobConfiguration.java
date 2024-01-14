package io.springbatch.springbatch.config.batch.mailJob;

import io.springbatch.springbatch.bdd.member.entity.Member;
import io.springbatch.springbatch.bdd.email.service.EmailService;
import io.springbatch.springbatch.config.batch.mailJob.validator.MailJobParameterValidator;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailException;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MailJobConfiguration {

    private final MailJobParameterValidator mailJobParameterValidator;
    private final EntityManagerFactory entityManagerFactory;
    private final StopWatchJobListener stopWatchJobListener;
    private final DataSource dataSource;
    private final EmailService emailService;

    @Bean
    public Job mailJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        log.info("mailJob Execute");
        return new JobBuilder("mailJob", jobRepository)
                .start(sendMailStep(jobRepository, platformTransactionManager))
                .validator(mailJobParameterValidator)
                .build();
    }

    @Bean
    public Step sendMailStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("sendMailStep", jobRepository)
                .<Member, Member>chunk(1000, platformTransactionManager)
                .listener(stopWatchJobListener)
                .reader(mailItemReader())
                .processor(mailAsyncItemProcessor())
                .writer(mailAsyncItemWriter())
                .faultTolerant()
                .retryLimit(5)
                .retry(MailException.class)
                .build();
    }

    @Bean
    public AsyncItemProcessor mailAsyncItemProcessor(
    ) {
        AsyncItemProcessor<Member, Member> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(mailItemProcessor(null, null));
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());

        return asyncItemProcessor;
    }

    @Bean
    @StepScope
    public ItemProcessor<Member, Member> mailItemProcessor(
            @Value("#{jobParameters[mailSubject]}") String subject,
            @Value("#{jobParameters[mailMessage]}") String mailMessage
    ) {
        return member -> {
            emailService.sendEmail(member.getEmail(), subject, mailMessage);
            return member;
        };
    }

    @Bean
    public ItemWriter<Member> mailItemWriter() {
        return new JdbcBatchItemWriterBuilder<Member>()
                .dataSource(dataSource)
                .sql("insert into member_copy values (:id, :name, :email)")
                .beanMapped()
                .build();
    }

    @Bean
    public AsyncItemWriter mailAsyncItemWriter() {
        AsyncItemWriter<Member> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(mailItemWriter());
        return asyncItemWriter;
    }

    @Bean
    public ItemReader<Member> mailItemReader() {
        log.info("itemReader {}", LocalDateTime.now());

        return new JdbcCursorItemReaderBuilder<Member>()
                .name("JdbcCursorItemReader")
                .fetchSize(1000)
                .sql("SELECT id, name, email FROM member")
                .rowMapper((rs, rowNum) -> {
                    return new Member(rs.getLong(1), rs.getString(2), rs.getString(3));
                })
                .dataSource(dataSource)
                .build();
    }
}
