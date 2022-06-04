package com.example.store;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.example.store.Builder.ExampleDTOBuilder;
import com.example.store.dto.user.CreateResetActivationTokenDTO;
import com.example.store.dto.user.CreateUserDTO;
import com.example.store.dto.user.RegistrationTokenDTO;
import com.example.store.exception.ValidationException;
import com.example.store.service.UserRegistrationService;


@SpringBootTest
public class UserRegistrationServiceTests {

    @Autowired
    private UserRegistrationService service;
    
    @Test
    @Transactional
    void createAndUserAndActiveTest() {
        ResponseEntity<RegistrationTokenDTO> token = service.registerUser(ExampleDTOBuilder.buildExampleUserDTO());
        service.activateUser(token.getBody().getToken());
    }

    @Test
    @Transactional
    void createAndSameNameUserTest() throws ValidationException{
        service.registerUser(ExampleDTOBuilder.buildExampleUserDTO());

        assertThrows(ValidationException.class, () -> {
            service.registerUser(new CreateUserDTO("ExampleName", "Examplepassword", "Example2@email.com"));
        });
    }

    @Test
    @Transactional
    void createAndSameEmailUserTest() throws ValidationException{
        service.registerUser(ExampleDTOBuilder.buildExampleUserDTO());

        assertThrows(ValidationException.class, () -> {
            service.registerUser(new CreateUserDTO("ExampleName2", "Examplepassword", "Example@email.com"));
        });
    }

    @Test
    @Transactional
    void createAndActiveWithNonExistingTokenTest() throws ValidationException{
        service.registerUser(ExampleDTOBuilder.buildExampleUserDTO());

        assertThrows(ValidationException.class, () -> {
            service.activateUser("Random Token");
        });
    }

    @Test
    @Transactional
    void createAndActiveTokenTwoTimesTest() throws ValidationException{
        ResponseEntity<RegistrationTokenDTO> token= service.registerUser(ExampleDTOBuilder.buildExampleUserDTO());
        service.activateUser(token.getBody().getToken());
        assertThrows(ValidationException.class, () -> {
            service.activateUser(token.getBody().getToken());
        });
    }

    @Test
    @Transactional
    void createandReactiveTokenTest() throws ValidationException{
       service.registerUser(ExampleDTOBuilder.buildExampleUserDTO());
        ResponseEntity<RegistrationTokenDTO> token = service.resetActivationToken(new CreateResetActivationTokenDTO("Example@email.com"));
        service.activateUser(token.getBody().getToken());
    }

    @Test
    @Transactional
    void reactiveNonExistingTokenTest() throws ValidationException{
        assertThrows(ValidationException.class, () -> {
            service.resetActivationToken(new CreateResetActivationTokenDTO("Example@email.com"));
        });     
    }

    @Test
    @Transactional
    void ReactiveTokenWithOtherEmailTest() throws ValidationException{
        service.registerUser(ExampleDTOBuilder.buildExampleUserDTO());
    
        assertThrows(ValidationException.class, () -> {
            service.resetActivationToken(new CreateResetActivationTokenDTO("Example2@email.com"));
        });  
    }

    @Test
    @Transactional
    void ReactiveActivatedTokenTest() throws ValidationException{
        ResponseEntity<RegistrationTokenDTO> token= service.registerUser(ExampleDTOBuilder.buildExampleUserDTO());
        service.activateUser(token.getBody().getToken());
    
        assertThrows(ValidationException.class, () -> {
            service.resetActivationToken(new CreateResetActivationTokenDTO("Example2@email.com"));
        });  
    }
}
