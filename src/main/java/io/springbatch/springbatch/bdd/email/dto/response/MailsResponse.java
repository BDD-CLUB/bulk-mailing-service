package io.springbatch.springbatch.bdd.email.dto.response;

import io.springbatch.springbatch.bdd.email.entity.Mail;
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
