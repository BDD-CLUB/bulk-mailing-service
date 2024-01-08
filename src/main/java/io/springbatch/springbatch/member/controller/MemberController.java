package io.springbatch.springbatch.member.controller;

import io.springbatch.springbatch.member.dto.response.FindAllMemberResponse;
import io.springbatch.springbatch.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Validated
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public String findMembers(Model model) {
        List<FindAllMemberResponse> findMembers = memberService.findMembers();
        model.addAttribute("members", findMembers);
        return "members";
    }



}
