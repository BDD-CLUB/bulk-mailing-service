package io.springbatch.springbatch.bdd.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(final String name);

    Optional<Member> findByEmail(final String email);
}
