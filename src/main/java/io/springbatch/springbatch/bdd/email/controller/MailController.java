package io.springbatch.springbatch.bdd.email.controller;

import io.springbatch.springbatch.bdd.email.dto.request.SaveMailRequest;
import io.springbatch.springbatch.bdd.email.dto.response.MailsResponse;
import io.springbatch.springbatch.bdd.email.entity.Mail;
import io.springbatch.springbatch.bdd.email.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping("/news-mail")
    public String mailForm() {
        return "save-mail";
    }

    @PostMapping("/news-mail")
    public ResponseEntity<String> saveMail(@RequestBody @Valid SaveMailRequest request) {
        mailService.saveMail(request.getTitle(), request.getText());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/news-mails");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/news-mail/{mailId}")
    public String updateMailForm(@PathVariable(name = "mailId") long mailId, Model model) {
        Mail findMail = mailService.findMail(mailId);
        model.addAttribute("title", findMail.getTitle());
        model.addAttribute("message", findMail.getMessage());

        return "update-mail";
    }

    @GetMapping("/news-mail/preview/{mailId}")
    public String savedMailForm(@PathVariable(name = "mailId") long mailId, Model model) {
        String savedForm = mailService.convertSaveMailMessage(mailId);
        model.addAttribute("message", savedForm);
        return "convert-mail";
    }

    @PatchMapping("/news-mail/{mailId}")
    public ResponseEntity<String> updateMail(
            @RequestBody @Valid SaveMailRequest request,
            @PathVariable(name = "mailId") Long mailId
    ) {
        mailService.updateMail(mailId, request.getTitle(), request.getText());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/news-mails");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/news-mail/{mailId}")
    public ResponseEntity<Void> deleteMail(@PathVariable(name = "mailId") long mailId) {
        mailService.deleteMail(mailId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/news-mails");

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/news-mails")
    public String mailsForm(Model model) {
        List<MailsResponse> mails = mailService.findMails();
        model.addAttribute("mails", mails);
        return "save-mails";
    }

    @PostMapping("/bulk-mail/{mailId}")
    public ResponseEntity<Void> sendBulkMail(@PathVariable(name = "mailId") long mailId) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        mailService.sendBulkMail(mailId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
