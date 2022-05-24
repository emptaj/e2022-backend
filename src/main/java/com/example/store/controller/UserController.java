package com.example.store.controller;

import com.example.store.dto.user.CreateUserDTO;
import com.example.store.entity.UserEntity;
import com.example.store.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/")
    private UserEntity registerUser(@RequestBody CreateUserDTO createUserDTO) {
        return userService.registerUser(createUserDTO);

    }
}
