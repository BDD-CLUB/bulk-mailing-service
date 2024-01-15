package io.springbatch.springbatch.bdd.email.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    @Builder
    public Mail(Long id, String title, String message) {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    public void update(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
