package io.springbatch.springbatch.email.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MonthRepostRequest {

    @NotEmpty(message = "수행할 배치 Job 이름을 입력해주세요.")
    private String jobName;

    @NotEmpty(message = "제목을 입력해주세요.")
    private String subject;

    @NotEmpty(message = "공지할 메시지를 입력해주세요.")
    private String reportMessage;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
}
