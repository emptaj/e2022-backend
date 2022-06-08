package com.example.store.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.store.service.JWTTokenService;
import com.example.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final String secret;
    private final UserService userService;
    private final JWTTokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getServletPath().equals("/login") || request.getServletPath().equals("/token/refresh")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    //settings
                    DecodedJWT decodedJWT = tokenService.getDecodedJWT(token, secret);
                    String username = decodedJWT.getSubject();

                    //loading user
                    UserDetails userDetails = userService.loadUserByUsername(username);
                    String[] authorities = decodedJWT.getClaim("authorities").asArray(String.class);
                    List<SimpleGrantedAuthority> authoritiesCollect = Arrays.stream(authorities).map(SimpleGrantedAuthority::new).
                            collect(Collectors.toList());

                    //authorization
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null,
                            authoritiesCollect);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    tokenService.prepareFailResponse(response, e.getMessage());
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
