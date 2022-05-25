package com.example.store.controller;

import com.example.store.dto.user.CreateUserDTO;
import com.example.store.entity.UserEntity;
import com.example.store.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users/")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    private String registerUser(@RequestBody CreateUserDTO createUserDTO) {
        return userService.registerUser(createUserDTO);

    }

    @PostMapping("/activate")
    private String activateUser(@RequestParam String activateToken) {
        return userService.activateUser(activateToken);
    }
}
