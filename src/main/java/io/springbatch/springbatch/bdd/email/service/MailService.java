package io.springbatch.springbatch.bdd.email.service;

import io.springbatch.springbatch.bdd.email.repository.MailRepository;
import io.springbatch.springbatch.bdd.email.dto.response.MailsResponse;
import io.springbatch.springbatch.bdd.email.entity.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MailService {

    private final MailRepository mailRepository;

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
                .orElseThrow();

        findMail.update(title, message);
    }
}
