package com.example.store.controller;

import com.example.store.dto.LoginCredentialsDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping("")
    public void login(@RequestBody LoginCredentialsDTO loginCredentialsDTO) {
         
    }
}
