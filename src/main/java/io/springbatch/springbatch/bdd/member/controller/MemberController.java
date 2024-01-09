package io.springbatch.springbatch.bdd.member.controller;

import io.springbatch.springbatch.bdd.member.dto.request.SaveMemberRequest;
import io.springbatch.springbatch.bdd.member.dto.response.FindAllMemberResponse;
import io.springbatch.springbatch.bdd.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public String saveMemberForm(HttpServletRequest request) {
        log.info(request.toString());
        return "member";
    }

    @PostMapping("/member")
    public String saveMember(@RequestBody SaveMemberRequest request) {

        return "ok";
    }


}
