package io.springbatch.springbatch.service;

import io.springbatch.springbatch.entity.Member;
import io.springbatch.springbatch.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

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

    public void deleteMember(Long memberId) {
        final Long findMemberId = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당하는 Id 값은 없습니다."))
                .getId();

        memberRepository.deleteById(findMemberId);
    }
}
