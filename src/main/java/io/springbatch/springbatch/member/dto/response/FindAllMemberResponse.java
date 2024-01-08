package io.springbatch.springbatch.member.dto.response;

import io.springbatch.springbatch.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindAllMemberResponse {

    private String name;
    private String email;

    public static List<FindAllMemberResponse> from(List<Member> members) {
        return members.stream()
                .map(member -> FindAllMemberResponse.builder()
                                .name(member.getName())
                                .email(member.getEmail())
                                .build()
                )
                .toList();
    }

}
