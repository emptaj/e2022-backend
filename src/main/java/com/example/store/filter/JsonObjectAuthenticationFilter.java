package com.example.store.filter;

import com.example.store.dto.LoginCredentialsDTO;
import com.example.store.entity.UserEntity;
import com.example.store.service.JWTTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@RequiredArgsConstructor
public class JsonObjectAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTTokenService tokenService;


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException,
            ServletException {
        UserDetails principal = (UserDetails) authResult.getPrincipal();
        UserEntity userEntity = (UserEntity) principal;
        String token = tokenService.createAccessToken(principal);
        String refreshToken = tokenService.createRefreshToken(principal);
        tokenService.prepareResponseWithTokens(response, token, refreshToken, userEntity.getId());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            LoginCredentialsDTO loginCredentialsDTO = objectMapper.readValue(sb.toString(), LoginCredentialsDTO.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginCredentialsDTO.getUsername(),
                    loginCredentialsDTO.getPassword());
            setDetails(request, authenticationToken);
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());

        }
    }
}
