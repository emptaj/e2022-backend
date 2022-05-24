package com.example.store.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "RegistrationTokens")
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    @OneToOne
    private UserEntity user;
}
