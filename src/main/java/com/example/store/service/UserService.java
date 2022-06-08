package com.example.store.service;

import com.example.store.entity.UserEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("User with given username %s does not exists",
                                username))
                );
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("User with given email %s does not exists",
                                email)));
    }

    public UserDetails getLoggedUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return (UserDetails) authentication.getPrincipal();

        }

        throw new ValidationException("Auth: user must be logged in");
    }

    public UserEntity getLoggedUserEntity() {
        UserDetails userDetails = getLoggedUserPrincipal();
        System.out.println(userDetails);
        return userRepository.findByUsername(userDetails.getUsername()).
                orElseThrow(
                        () -> new UsernameNotFoundException("Auth: user must be logged in")
                );
    }

    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(UserEntity.class, userId));
    }
}
