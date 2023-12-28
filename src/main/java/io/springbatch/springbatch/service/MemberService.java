package io.springbatch.springbatch.service;

import io.springbatch.springbatch.dto.MemberResponse;
import io.springbatch.springbatch.entity.Member;
import io.springbatch.springbatch.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(final String name, final String email) {
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

    @Transactional
    public void deleteMember(final Long memberId) {
        final Long findMemberId = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당하는 Id 값은 없습니다."))
                .getId();

        memberRepository.deleteById(findMemberId);
    }

    public Page<Member> findAllMember(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }
}
