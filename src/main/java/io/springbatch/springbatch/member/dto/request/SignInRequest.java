package io.springbatch.springbatch.member.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SignInRequest {

    @NotEmpty(message = "아이디를 입력해주세요.")
    private String memberId;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

}
