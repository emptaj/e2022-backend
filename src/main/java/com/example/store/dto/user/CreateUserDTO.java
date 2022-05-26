package com.example.store.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    @NotNull(message = "Username should not be empty")
    @Size(min = 5, message = "Username should has at least 5 characters")
    private String username;

    @NotNull(message = "Password should not be empty")
    @Size(min = 8, message = "User password should has at least 8 characters")
    private String password;

    @NotNull(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;
}
