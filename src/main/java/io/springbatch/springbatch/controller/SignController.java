package io.springbatch.springbatch.controller;

import io.springbatch.springbatch.dto.DeleteMemberRequest;
import io.springbatch.springbatch.dto.SignUpRequest;
import io.springbatch.springbatch.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignController {

    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequest request) {
        memberService.signUp(request.getName(), request.getEmail());

        return ResponseEntity.ok("생성완료");
    }

    @DeleteMapping("/member")
    public ResponseEntity<Void> deleteMember(@RequestBody @Valid DeleteMemberRequest request) {
        memberService.deleteMember(request.getMemberId());

        return ResponseEntity.noContent().build();
    }

}
