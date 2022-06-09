package com.example.store.entity;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "RegistrationTokens")
public class RegistrationTokenEntity {
    @Id
    @SequenceGenerator(name = "sequence_registration_token_id", sequenceName = "sequence_registration_token_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_registration_token_id")
    private Long id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    @Nullable
    private LocalDateTime confirmedAt;
    @OneToOne
    private UserEntity user;

    public RegistrationTokenEntity(String token, LocalDateTime createdAt, LocalDateTime expiresAt, UserEntity user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}
