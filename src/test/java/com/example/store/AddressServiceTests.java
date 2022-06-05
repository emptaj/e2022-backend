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
import com.example.store.exception.NotFoundException;
import com.example.store.service.AddressService;

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
        AddressDTO address = ExampleDTOBuilder.BuildExampleAddress();
        AddressDTO createdAdress = service.createAddress(address);

        assertTrue(EqualDTOChecker.ifAddressEquals(address, createdAdress));
    }

    @Test
    @Transactional
    void CreateFindAndDeleteAddressTest() throws NotFoundException{
        AddressDTO address = ExampleDTOBuilder.BuildExampleAddress();
        AddressDTO createdAddress = service.createAddress(address);
        AddressDTO foundedAddress = service.getAddress(createdAddress.getId());

        assertTrue(EqualDTOChecker.ifAddressEquals(address, createdAddress));
        assertTrue(EqualDTOChecker.ifAddressEquals(address, foundedAddress));
    }

    @Test
    @Transactional
    void UpdateCreatedAddressTest() throws NotFoundException{
        AddressDTO address = ExampleDTOBuilder.BuildExampleAddress();
        AddressDTO createdAddress = service.createAddress(address);
        
        AddressDTO updated = service.updateAddress(createdAddress.getId(), ExampleDTOBuilder.BuildExampleUpdateAddress());
        AddressDTO foundedAddress = service.getAddress(createdAddress.getId());

        assertTrue(EqualDTOChecker.ifAddressEquals(updated, foundedAddress));
        assertTrue(!EqualDTOChecker.ifAddressEquals(address, foundedAddress));
    }    

}
