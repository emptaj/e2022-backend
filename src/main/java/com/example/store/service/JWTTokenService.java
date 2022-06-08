package com.example.store.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.store.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class JWTTokenService {
    private final UserService userService;
    private final String secret;
    private final Long expirationTime;

    public JWTTokenService(UserService userService, @Value("${jwt.secret}")
            String secret, @Value("${jwt.expirationTime}") Long expirationTime) {
        this.userService = userService;
        this.secret = secret;
        this.expirationTime = expirationTime;
    }

    public String createAccessToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(Algorithm.HMAC256(secret));
    }

    public String createRefreshToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime * 2))
                .sign(Algorithm.HMAC256(secret));
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                //settings
                String username = getDecodedJWT(refresh_token, secret).getSubject();

                //loading user
                UserDetails userDetails = userService.loadUserByUsername(username);
                UserEntity userEntity = (UserEntity) userDetails;
                String access_token = createAccessToken(userDetails);
                //preparing response
                prepareResponseWithTokens(response, access_token, refresh_token, userEntity.getId());

            } catch (Exception e) {
                prepareFailResponse(response, e.getMessage());
            }
        } else {
            prepareFailResponse(response, "token is empty");
        }
    }

    public DecodedJWT getDecodedJWT(String token, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);

    }

    public void prepareResponseWithTokens(HttpServletResponse response,
                                          String access_token,
                                          String refresh_token, Long userId) throws IOException {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", "Bearer " + access_token);
        tokens.put("refresh_token", "Bearer " + refresh_token);
        tokens.put("user_id", String.valueOf(userId));
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader("Authorization", "Bearer " + access_token);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    public void prepareFailResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", errorMessage);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
