package io.springbatch.springbatch.bdd.email.repository;

import io.springbatch.springbatch.bdd.email.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Mail, Long> {

}
