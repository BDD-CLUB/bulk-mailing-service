package io.springbatch.springbatch.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeleteMemberRequest {

    @NotNull(message = "회원의 Id값을 입력해주세요.")
    private Long memberId;

}
