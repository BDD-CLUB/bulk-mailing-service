package io.springbatch.springbatch.controller;

import io.springbatch.springbatch.dto.DeleteMemberRequest;
import io.springbatch.springbatch.dto.MemberResponse;
import io.springbatch.springbatch.dto.SignUpRequest;
import io.springbatch.springbatch.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
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

    @PostMapping
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequest request) {
        memberService.signUp(request.getName(), request.getEmail());

        return ResponseEntity.ok("생성완료");
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@RequestBody @Valid DeleteMemberRequest request) {
        memberService.deleteMember(request.getMemberId());

        return ResponseEntity.noContent().build();
    }

}
