package io.springbatch.springbatch.member.service;

import io.springbatch.springbatch.member.entity.Member;
import io.springbatch.springbatch.member.entity.MemberRepository;
import io.springbatch.springbatch.member.entity.password.Password;
import io.springbatch.springbatch.member.entity.password.PasswordFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void signIn(final String memberId, final String rawPassword) {
        Member findMember = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException(memberId + "는 없는 계정입니다."));

        if (findMember.getPassword().isWrongPassword(rawPassword)) {
            throw new RuntimeException(rawPassword + "는 잘못된 비밀번호 입니다.");
        }

        System.out.println("로그인 성공");
    }

    @Transactional
    public void signUp(final String name, final String memberId, final String password, final String email) {
        memberRepository.findByName(name)
                .ifPresent((member) -> {
                    throw new RuntimeException(member.getName() + "은 이미 있습니다.");
                });

        memberRepository.save(Member.builder()
                .name(name)
                .memberId(memberId)
                .password(Password.from(password))
                .email(email)
                .role(Member.MemberRoleType.ROLE_USER)
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
