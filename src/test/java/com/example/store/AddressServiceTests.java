package com.example.store;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.store.Builder.ExampleDTOBuilder;
import com.example.store.EqualChecker.EqualDTOChecker;
import com.example.store.dto.address.AddressDTO;
import com.example.store.exception.NotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.service.AddressService;

@SpringBootTest
class AddressServiceTests {
    
    @Autowired
    private AddressService service;

    @Test
    void getNonExistingAddressByIdTest() throws NotFoundException{
        Long id = -1L;

        assertThrows(NotFoundException.class, () -> {
            service.getSingleAddress(id);
        });
    }

    @Test
    void getExistingAddressByIdTest() throws NotFoundException{
        AddressDTO address = ExampleDTOBuilder.BuildExampleAddress();
        AddressDTO createdAdress = service.createAddress(address);
        service.deleteAddress(createdAdress.getId());

        assertTrue(EqualDTOChecker.ifAddressEquals(address, createdAdress));
    }

    @Test
    void CreateFindAndDeleteAddressTest() throws NotFoundException{
        AddressDTO address = ExampleDTOBuilder.BuildExampleAddress();
        AddressDTO createdAddress = service.createAddress(address);
        AddressDTO foundedAddress = service.getSingleAddress(createdAddress.getId());
        service.deleteAddress(createdAddress.getId());

        assertTrue(EqualDTOChecker.ifAddressEquals(address, createdAddress));
        assertTrue(EqualDTOChecker.ifAddressEquals(address, foundedAddress));
    }

    @Test
    void getDeletedAddressTest() throws NotFoundException{
        AddressDTO address = ExampleDTOBuilder.BuildExampleAddress();
        AddressDTO createdAddress = service.createAddress(address);
        service.deleteAddress(createdAddress.getId());

        assertThrows(NotFoundException.class, () -> {
            service.getSingleAddress(createdAddress.getId());
        });
    }

}
