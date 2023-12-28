package io.springbatch.springbatch.service;

import io.springbatch.springbatch.entity.Member;
import io.springbatch.springbatch.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final MemberRepository memberRepository;

    public void signUp(String name, String email) {
        memberRepository.findByName(name)
                .ifPresent((member) -> {
                    throw new RuntimeException(member.getName() + "은 이미 있습니다.");
                });

        memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .pushAgree(true)
                .build());
    }

}
