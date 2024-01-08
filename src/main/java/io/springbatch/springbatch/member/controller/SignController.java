package io.springbatch.springbatch.member.controller;

import io.springbatch.springbatch.member.dto.request.DeleteMemberRequest;
import io.springbatch.springbatch.member.dto.response.MemberResponse;
import io.springbatch.springbatch.member.dto.request.SignUpRequest;
import io.springbatch.springbatch.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class SignController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<Page<MemberResponse>> findMembers(
            @RequestParam(defaultValue = "0", value = "page") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10", value = "size") @PositiveOrZero @Max(30) int size
    ) {
        return ResponseEntity.ok(memberService.findAllMember(PageRequest.of(page, size))
                .map(MemberResponse::from));
    }

    @GetMapping("/sign-up")
    public String signUpPage() {
        log.info("GET login");
        return "signUp";
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest request) {
        log.info("POST login");

        memberService.signUp(
                request.getName(),
                request.getMemberId(),
                request.getPassword(),
                request.getEmail()
        );

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@RequestBody @Valid DeleteMemberRequest request) {
        memberService.deleteMember(request.getMemberId());

        return ResponseEntity.noContent().build();
    }

}
