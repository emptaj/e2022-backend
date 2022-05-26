package com.example.store.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RegistrationTokenDTO {

    public static final String CONFIRMATION_PATH = "http://localhost:8080/api/users/activate?activationToken=%s";

    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String confirmationPath;

    public static String createConfirmationPath(String token) {
        return String.format(CONFIRMATION_PATH, token);
    }

}
