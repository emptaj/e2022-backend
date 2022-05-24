package com.example.store.service;

import com.example.store.dto.user.CreateUserDTO;
import com.example.store.entity.RegistrationTokenEntity;
import com.example.store.entity.UserEntity;
import com.example.store.entity.enums.UserRole;
import com.example.store.exception.ValidationException;
import com.example.store.mapper.UserMapper;
import com.example.store.repository.RegistrationTokenRepository;
import com.example.store.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RegistrationTokenRepository registrationTokenRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("User with given username %s does not exists",
                                username))
                );
    }

    public UserEntity registerUser(CreateUserDTO createUserDTO) {
        Optional<UserEntity> byUsername = userRepository.findByUsername(createUserDTO.getUsername());

        if (byUsername.isPresent())
            throw new ValidationException(String.format("User with username %s already exists!",
                    createUserDTO.getUsername()));

        else {
            Optional<UserEntity> byEmail = userRepository.findByEmail(createUserDTO.getEmail());

            if (byEmail.isPresent())
                throw new ValidationException(String.format("User with email %s already exists!",
                        createUserDTO.getEmail()));
            else {
                String encoded = bCryptPasswordEncoder.encode(createUserDTO.getPassword());
                createUserDTO.setPassword(encoded);
                UserEntity userEntity = userMapper.toEntity(createUserDTO);
                userEntity.setUserRole(UserRole.CLIENT);
                userRepository.save(userEntity);

                //RegistrationToken
                String token = String.valueOf(UUID.randomUUID());
                LocalDateTime creationAt = LocalDateTime.now();
                LocalDateTime expiresAt = creationAt.plusMinutes(20);

                RegistrationTokenEntity registrationTokenEntity = new RegistrationTokenEntity(null,
                        token,
                        creationAt,
                        expiresAt,
                        userEntity);

                registrationTokenRepository.save(registrationTokenEntity);
                return userEntity;
            }
        }

    }


}
