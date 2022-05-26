package com.example.store.service;

import com.example.store.dto.user.CreateUserDTO;
import com.example.store.dto.user.RegistrationTokenDTO;
import com.example.store.dto.user.CreateResetActivationTokenDTO;
import com.example.store.entity.RegistrationTokenEntity;
import com.example.store.entity.UserEntity;
import com.example.store.entity.enums.UserRole;
import com.example.store.exception.ValidationException;
import com.example.store.mapper.RegistrationTokenMapper;
import com.example.store.mapper.UserMapper;
import com.example.store.repository.RegistrationTokenRepository;
import com.example.store.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserRegistrationService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final RegistrationTokenRepository registrationTokenRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseEntity<RegistrationTokenDTO> registerUser(CreateUserDTO createUserDTO) {
        Optional<UserEntity> byUsername = userRepository.findByUsername(createUserDTO.getUsername());

        if (byUsername.isPresent())
            throw new ValidationException(String.format("User with username %s already exists!",
                    createUserDTO.getUsername()));

        Optional<UserEntity> byEmail = userRepository.findByEmail(createUserDTO.getEmail());

        if (byEmail.isPresent())
            throw new ValidationException(String.format("User with email %s already exists!",
                    createUserDTO.getEmail()));

        String encoded = bCryptPasswordEncoder.encode(createUserDTO.getPassword());
        createUserDTO.setPassword(encoded);
        UserEntity userEntity = userMapper.toEntity(createUserDTO);
        userEntity.setUserRole(UserRole.CLIENT);
        userRepository.save(userEntity);

        //RegistrationToken
        String token = String.valueOf(UUID.randomUUID());
        LocalDateTime creationAt = LocalDateTime.now();
        LocalDateTime expiresAt = creationAt.plusMinutes(20);

        RegistrationTokenEntity registrationTokenEntity = new RegistrationTokenEntity(
                token,
                creationAt,
                expiresAt,
                userEntity);

        registrationTokenRepository.save(registrationTokenEntity);

        RegistrationTokenDTO registrationTokenDTO = RegistrationTokenMapper.INSTANCE.toDTO(registrationTokenEntity);
        return new ResponseEntity(registrationTokenDTO, HttpStatus.CREATED);

    }

    @Transactional
    public String activateUser(String activateToken) {
        RegistrationTokenEntity registrationTokenEntity = registrationTokenRepository.findByToken(activateToken).
                orElseThrow(
                        () -> new ValidationException("Token does not exists!")
                );

        if (registrationTokenEntity.getConfirmedAt() != null)
            throw new ValidationException("Account has been already activated!");

        LocalDateTime now = LocalDateTime.now();

        if (registrationTokenEntity.getExpiresAt().isBefore(now))
            throw new ValidationException("Token expired!");

        UserEntity user = registrationTokenEntity.getUser();
        user.setEnabled(true);
        registrationTokenEntity.setConfirmedAt(now);

        return "Account has been activated!";
    }

    @Transactional
    public ResponseEntity<RegistrationTokenDTO> resetActivationToken(CreateResetActivationTokenDTO resetActivationLinkDTO) {
        UserEntity userEntity = userRepository.findByEmail(resetActivationLinkDTO.getEmail()).
                orElseThrow(
                        () -> new ValidationException("Email not found")
                );

        RegistrationTokenEntity registrationTokenEntity = registrationTokenRepository.findByUser(userEntity)
                .orElseThrow(
                        () -> new ValidationException("Email not found")
                );

        if (registrationTokenEntity.getConfirmedAt() != null) {
            throw new ValidationException("Account has been already activated!");
        }

        LocalDateTime now = LocalDateTime.now();
        registrationTokenEntity.setToken(String.valueOf(UUID.randomUUID()));
        registrationTokenEntity.setCreatedAt(now);
        registrationTokenEntity.setExpiresAt(now.plusMinutes(20));

        return new ResponseEntity(RegistrationTokenMapper.INSTANCE.toDTO(registrationTokenEntity), HttpStatus.ACCEPTED);

    }
}
