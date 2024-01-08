package io.springbatch.springbatch.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @NotEmpty(message = "닉네임을 입력해주세요.")
    private String name;

    @NotEmpty(message = "아이디를 입력해주세요.")
    private String memberId;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    @Email
    private String email;

}
