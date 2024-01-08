package io.springbatch.springbatch.email.controller;

import io.springbatch.springbatch.email.dto.request.SaveMailRequest;
import io.springbatch.springbatch.email.dto.response.MailsResponse;
import io.springbatch.springbatch.email.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
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

    @ResponseBody
    @PostMapping("/mail")
    public ResponseEntity<String> saveMail(@RequestBody @Valid SaveMailRequest request) {
        log.info("title = {}, body = {}", request.getTitle(), request.getText());
        mailService.saveMail(request.getTitle(), request.getText());

        return ResponseEntity.ok("저장 완료");
    }

    @GetMapping("/mails")
    public String mailsForm(Model model) {
        List<MailsResponse> mails = mailService.findMails();

        log.info("mails = {}", mails.toString());

        model.addAttribute("mails", mails);
        return "save-mails";
    }

}
