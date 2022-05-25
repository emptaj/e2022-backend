package com.example.store.controller;

import com.example.store.dto.user.CreateUserDTO;
import com.example.store.entity.UserEntity;
import com.example.store.exception.ValidationException;
import com.example.store.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users/")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    private String registerUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return userService.registerUser(createUserDTO);

    }

    @PostMapping("/activate")
    private String activateUser(@RequestParam String activateToken) {
        return userService.activateUser(activateToken);
    }
}
