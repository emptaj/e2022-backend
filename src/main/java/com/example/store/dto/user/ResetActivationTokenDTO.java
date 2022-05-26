package com.example.store.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class ResetActivationTokenDTO {

    @NotNull(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;
}
