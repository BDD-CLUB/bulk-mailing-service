package io.springbatch.springbatch.email.dto.response;

import io.springbatch.springbatch.email.entity.Mail;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MailsResponse {

    private Long id;
    private String title;
    private String message;

    public static MailsResponse from(Mail mail) {
        return MailsResponse.builder()
                .id(mail.getId())
                .title(mail.getTitle())
                .message(mail.getMessage())
                .build();
    }

}
