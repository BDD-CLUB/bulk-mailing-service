package io.springbatch.springbatch.member.controller;

import io.springbatch.springbatch.member.dto.SignInRequest;
import io.springbatch.springbatch.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class SignInController {

    private final MemberService memberService;

    @GetMapping("/sign-in")
    public String signIn() {
        return "login";
    }

    @ResponseBody
    @PostMapping("/sign-in")
    public ResponseEntity<HttpStatus> signIn(@RequestBody SignInRequest request) {
        memberService.signIn(request.getMemberId(), request.getPassword());

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

}
