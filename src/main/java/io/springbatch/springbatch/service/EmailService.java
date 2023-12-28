package io.springbatch.springbatch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public sendEmail(String to, String subject, String text) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setFrom("02ggang9@gmail.com");
        emailMessage.setTo(to);
        emailMessage.setSubject(subject);
        emailMessage.setText(text);
        javaMailSender.send(emailMessage);
    }

}
