package io.springbatch.springbatch.service;

import io.springbatch.springbatch.IntegrationTest;
import io.springbatch.springbatch.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SignUpServiceTest extends IntegrationTest {

    @Test
    @DisplayName("성공적으로 멤버가 저장되어야 한다.")
    void 성공적으로_멤버가_저장되어야_한다() {
        Member member = Member.builder()
                .name("ggang9")
                .email("testggang9@gmail.com")
                .pushAgree(true)
                .build();

        signUpService.signUp("ggang9", "testggang9@gmail.com");

        em.flush();
        em.clear();

        Member findMember = memberRepository.findByName("ggang9")
                .orElseThrow();

        Assertions.assertThat(findMember.getName()).isEqualTo("ggang9");
    }

}
