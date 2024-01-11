package io.springbatch.springbatch.config.batch.mailJob;

import io.springbatch.springbatch.bdd.member.entity.Member;
import io.springbatch.springbatch.bdd.email.service.EmailService;
import io.springbatch.springbatch.config.batch.mailJob.validator.MailJobParameterValidator;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MailJobConfiguration {

    private final MailJobParameterValidator mailJobParameterValidator;
    private final EntityManagerFactory entityManagerFactory;
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
                .<Member, Member>chunk(20, platformTransactionManager)
                .reader(mailItemReader())
                .processor(mailItemProcessor(null, null))
                .writer(mailItemWriter())
                .faultTolerant()
                .retryLimit(5)
                .retry(MailException.class)
                .build();
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
        return items -> items.forEach(member -> System.out.println("Send to " + member.getEmail()));
    }

    @Bean
    public ItemReader<Member> mailItemReader() {
        return new JpaPagingItemReaderBuilder<Member>()
                .name("memberMailReader")
                .entityManagerFactory(this.entityManagerFactory)
                .pageSize(20)
                .queryString("SELECT m FROM Member m")
                .build();
    }
}
