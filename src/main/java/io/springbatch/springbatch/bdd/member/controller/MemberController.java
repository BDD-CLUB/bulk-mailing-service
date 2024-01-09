package io.springbatch.springbatch.bdd.member.controller;

import io.springbatch.springbatch.bdd.member.dto.request.SaveMemberRequest;
import io.springbatch.springbatch.bdd.member.dto.response.FindAllMemberResponse;
import io.springbatch.springbatch.bdd.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public String findMembers(Model model) {
        List<FindAllMemberResponse> findMembers = memberService.findMembers();
        model.addAttribute("members", findMembers);
        return "members";
    }

    @GetMapping("/member")
    public String saveMemberForm() {
        return "member";
    }

    @PostMapping("/member")
    @ResponseBody
    public ResponseEntity<Void> saveMember(@RequestBody @Valid SaveMemberRequest request) {
        memberService.saveMember(request.getNickname(), request.getEmail());
        log.info("{}, {}", request.getNickname(), request.getEmail());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

}
