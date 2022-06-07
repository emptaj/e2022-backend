package com.example.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginCredentialsDTO {
    private String username;
    private String password;
}
