package io.springbatch.springbatch.email.repository;

import io.springbatch.springbatch.email.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Mail, Long> {

}
