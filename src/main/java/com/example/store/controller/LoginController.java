package com.example.store.controller;

import com.example.store.dto.LoginCredentialsDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping("")
    public void login(@RequestBody LoginCredentialsDTO loginCredentialsDTO) {

    }
}
