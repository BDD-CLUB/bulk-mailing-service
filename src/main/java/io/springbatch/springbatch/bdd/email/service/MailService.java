package io.springbatch.springbatch.bdd.email.service;

import io.springbatch.springbatch.bdd.email.entity.MailFormatConverter;
import io.springbatch.springbatch.bdd.email.entity.MdFormatConverter;
import io.springbatch.springbatch.bdd.email.repository.MailRepository;
import io.springbatch.springbatch.bdd.email.dto.response.MailsResponse;
import io.springbatch.springbatch.bdd.email.entity.Mail;
import io.springbatch.springbatch.config.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MailService {

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
                .orElseThrow();

        findMail.update(title, message);
    }

    public String convertSaveMailMessage(Long mailId) {
        Mail findMail = mailRepository.findById(mailId)
                .orElseThrow(() -> new BusinessException(mailId + "에 해당하는 메일을 찾을 수 없습니다"));

        return mdFormatConverter.convert(findMail.getMessage());
    }
}
