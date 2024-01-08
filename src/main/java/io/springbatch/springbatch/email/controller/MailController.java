package io.springbatch.springbatch.email.controller;

import io.springbatch.springbatch.email.dto.request.SaveMailRequest;
import io.springbatch.springbatch.email.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@Controller
@RequestMapping("/admin/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping
    public String mailForm() {
        return "save-mail";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> saveMail(@RequestBody @Valid SaveMailRequest request) {
        log.info("title = {}, body = {}", request.getTitle(), request.getText());
        mailService.saveMail(request.getTitle(), request.getText());

        return ResponseEntity.ok("저장 완료");
    }

}
