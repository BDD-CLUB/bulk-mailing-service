package io.springbatch.springbatch.member.entity;

import io.springbatch.springbatch.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(final String name);

    Optional<Member> findByMemberId(final String memberId);

}
