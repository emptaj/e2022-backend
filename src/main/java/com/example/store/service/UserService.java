package com.example.store.service;

import com.example.store.dto.user.CreateUserDTO;
import com.example.store.entity.UserEntity;
import com.example.store.entity.enums.UserRole;
import com.example.store.mapper.UserMapper;
import com.example.store.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("User with given username %s does not exists", username))
                );
    }

    public UserEntity registerUser(CreateUserDTO createUserDTO) {
        Optional<UserEntity> byUsername = userRepository.findByUsername(createUserDTO.getUsername());

        if (byUsername.isPresent())
            throw new RuntimeException(String.format("User with username  %s already exists!", createUserDTO.getUsername()));

        else {
            String encoded = bCryptPasswordEncoder.encode(createUserDTO.getPassword());
            createUserDTO.setPassword(encoded);
            UserEntity userEntity = userMapper.toEntity(createUserDTO);
            userEntity.setUserRole(UserRole.CLIENT);
            userRepository.save(userEntity);
            return userEntity;
        }

    }


}
