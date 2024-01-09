package io.springbatch.springbatch.bdd.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SaveMemberRequest {

    @NotEmpty(message = "닉네임을 입력해주세요.")
    private String nickName;

    @Email(message = "올바른 이메일을 입력해주세요.")
    private String email;

}
