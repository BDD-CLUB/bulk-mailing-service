package io.springbatch.springbatch.bdd.email.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SaveMailRequest {

    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "제목을 입력해주세요.")
    private String text;

}
