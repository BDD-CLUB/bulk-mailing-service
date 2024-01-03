package io.springbatch.springbatch.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SignInController {

    @GetMapping("/login")
    public String signIn() {
        return "login";
    }

}
