package io.springbatch.springbatch.bdd.member.service;

import io.springbatch.springbatch.bdd.member.dto.response.FindAllMemberResponse;
import io.springbatch.springbatch.bdd.member.entity.Member;
import io.springbatch.springbatch.bdd.member.entity.MemberRepository;
import io.springbatch.springbatch.config.exception.BusinessException;
import io.springbatch.springbatch.member.entity.password.Password;
import io.springbatch.springbatch.member.service.AuthCookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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

    public List<FindAllMemberResponse> findMembers() {
        return FindAllMemberResponse.from(memberRepository.findAll());
    }

    @Transactional
    public void saveMember(final String nickName, final String email) {
        memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(email + "은 중복된 이메일 입니다."));

        memberRepository.save(Member.builder()
                .name(nickName)
                .email(email)
                .build());
    }
}
