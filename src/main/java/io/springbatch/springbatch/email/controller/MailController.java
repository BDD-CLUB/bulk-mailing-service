package io.springbatch.springbatch.email.controller;

import io.springbatch.springbatch.email.dto.request.SaveMailRequest;
import io.springbatch.springbatch.email.dto.response.MailsResponse;
import io.springbatch.springbatch.email.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping("/mail")
    public String mailForm() {
        return "save-mail";
    }

    @PostMapping("/mail")
    public ResponseEntity<String> saveMail(@RequestBody @Valid SaveMailRequest request) {
        mailService.saveMail(request.getTitle(), request.getText());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/admin/mails");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/mails")
    public String mailsForm(Model model) {
        List<MailsResponse> mails = mailService.findMails();

        log.info("mails = {}", mails.toString());

        model.addAttribute("mails", mails);
        return "save-mails";
    }

}
