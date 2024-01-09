package io.springbatch.springbatch.bdd.mail.controller;

import io.springbatch.springbatch.email.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Valid
@Controller
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping("/mail")
    public String mailForm() {

        return ""
    }

}
