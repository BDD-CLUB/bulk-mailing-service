package io.springbatch.springbatch.bdd.email.service;

import io.springbatch.springbatch.bdd.email.entity.MailFormatConverter;
import io.springbatch.springbatch.bdd.email.entity.MdFormatConverter;
import io.springbatch.springbatch.bdd.email.repository.MailRepository;
import io.springbatch.springbatch.bdd.email.dto.response.MailsResponse;
import io.springbatch.springbatch.bdd.email.entity.Mail;
import io.springbatch.springbatch.config.exception.BusinessException;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MailService {

    private final JobLauncher jobLauncher;
    private final ApplicationContext context;
    private final MailRepository mailRepository;
    private final MdFormatConverter mdFormatConverter;

    @Transactional
    public void saveMail(String title, String message) {
        mailRepository.save(
                Mail.builder()
                        .title(title)
                        .message(message)
                        .build()
        );
    }

    public List<MailsResponse> findMails() {
        List<Mail> findMails = mailRepository.findAll();

        return findMails.stream()
                .map(MailsResponse::from)
                .toList();

    }

    @Transactional
    public void updateMail(Long mailId, String title, String message) {
        Mail findMail = mailRepository.findById(mailId)
                .orElseThrow(() -> new BusinessException(mailId + "에 해당하는 메일을 찾을 수 없습니다."));

        findMail.update(title, message);
    }

    public String convertSaveMailMessage(long mailId) {
        Mail findMail = mailRepository.findById(mailId)
                .orElseThrow(() -> new BusinessException(mailId + "에 해당하는 메일을 찾을 수 없습니다."));

        return mdFormatConverter.convert(findMail.getMessage());
    }

    public Mail findMail(long mailId) {
        return mailRepository.findById(mailId)
                .orElseThrow(() -> new BusinessException(mailId + "에 해당하는 메일을 찾을 수 없습니다"));
    }

    @Transactional
    public void deleteMail(long mailId) {
        mailRepository.deleteById(mailId);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void sendBulkMail(long mailId) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Mail findMail = mailRepository.findById(mailId)
                .orElseThrow(() -> new BusinessException(mailId + "에 해당하는 메일을 찾을 수 없습니다"));

        startBulkMailJob(findMail.getTitle(), findMail.getMessage());
    }

    private void startBulkMailJob(String title, String message) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        final Job findJob = context.getBean("mailJob", Job.class);

        Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        JobParameters jobParameter = new JobParametersBuilder()
                .addDate("startTime", date)
                .addString("randomStringForTest", generateRandomString()) // 실제 운영에서는 삭제할 것.
                .addString("mailSubject", title)
                .addString("mailMessage", message)
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
