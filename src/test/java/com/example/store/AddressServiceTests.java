package com.example.store;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.store.Builder.ExampleDTOBuilder;
import com.example.store.EqualChecker.EqualDTOChecker;
import com.example.store.dto.address.AddressDTO;
import com.example.store.dto.address.CreateAddressDTO;
import com.example.store.exception.NotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.service.AddressService;
import com.example.store.validator.Validator;

@SpringBootTest
class AddressServiceTests {
    
    @Autowired
    private AddressService service;

    
    @Test
    @Transactional
    void getNonExistingAddressByIdTest() throws NotFoundException{
        Long id = -1L;

        assertThrows(NotFoundException.class, () -> {
            service.getAddress(id);
        });
    }

    @Test
    @Transactional
    void getExistingAddressByIdTest() throws NotFoundException{
        CreateAddressDTO address = ExampleDTOBuilder.BuildExampleAddress();
        AddressDTO createdAdress = service.createAddress(address);

        assertTrue(EqualDTOChecker.ifAddressEquals(address, createdAdress));
    }

    @Test
    @Transactional
    void CreateFindAndDeleteAddressTest() throws NotFoundException{
        CreateAddressDTO address = ExampleDTOBuilder.BuildExampleAddress();
        AddressDTO createdAddress = service.createAddress(address);
        AddressDTO foundedAddress = service.getAddress(createdAddress.getId());

        assertTrue(EqualDTOChecker.ifAddressEquals(address, createdAddress));
        assertTrue(EqualDTOChecker.ifAddressEquals(address, foundedAddress));
    }

    @Test
    @Transactional
    void UpdateCreatedAddressTest() throws NotFoundException{
        CreateAddressDTO address = ExampleDTOBuilder.BuildExampleAddress();
        AddressDTO createdAddress = service.createAddress(address);
        
        AddressDTO updated = service.updateAddress(createdAddress.getId(), CreateAddressDTO.builder()
        .country("Polska")
        .city("Lublin")
        .postalCode("20-802")
        .street("Niebieska")
        .houseNum("10")
        .flatNum("12")
        .phone("123456789")
        .build());
        AddressDTO foundedAddress = service.getAddress(createdAddress.getId());

        assertTrue(EqualDTOChecker.ifAddressEquals(updated, foundedAddress));
        assertTrue(!EqualDTOChecker.ifAddressEquals(address, foundedAddress));
    }

    @Test
    @Transactional
    void CreateInvalidAddressTest() throws ValidationException{
        CreateAddressDTO address = CreateAddressDTO.builder()
        .country("Polska")
        .postalCode("20-802")
        .street("Niebieska")
        .houseNum("11")
        .flatNum("12")
        .phone("123456789")
        .build();


       

        assertThrows(ValidationException.class, () -> {
            Validator.validate(address);
        });

    }

}
