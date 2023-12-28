package io.springbatch.springbatch.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @NotEmpty(message = "닉네임을 입력해주세요.")
    private String name;

    @Email
    private String email;

}
