package com.example.store.controller;

import com.example.store.dto.user.CreateResetActivationTokenDTO;
import com.example.store.dto.user.CreateUserDTO;
import com.example.store.dto.user.RegistrationTokenDTO;
import com.example.store.service.UserRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/users/")
@AllArgsConstructor
@CrossOrigin
public class UserRegistrationController {
    private final UserRegistrationService userRegistrationService;

    @PostMapping("/")
    private ResponseEntity<RegistrationTokenDTO> registerUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return userRegistrationService.registerUser(createUserDTO);

    }

    @PostMapping("/activate")
    private String activateUser(@RequestParam String activationToken) {
        return userRegistrationService.activateUser(activationToken);
    }

    @PostMapping("/activate/reset")
    private ResponseEntity<RegistrationTokenDTO> resetActivationToken(
            @Valid
            @RequestBody CreateResetActivationTokenDTO resetActivationLinkDTO) {
        return userRegistrationService.resetActivationToken(resetActivationLinkDTO);
    }
}
