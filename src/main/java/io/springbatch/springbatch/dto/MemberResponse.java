package io.springbatch.springbatch.dto;

import io.springbatch.springbatch.entity.Member;
import lombok.Builder;

@Builder
public class MemberResponse {

    private String name;
    private String email;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}
