package com.example.store;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.store.dto.address.AddressDTO;
import com.example.store.exception.NotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.service.AddressService;

@SpringBootTest
class AddressServiceTests {
    
    @Autowired
    private AddressService service;

    private AddressDTO BuildExampleAdress(){
        return AddressDTO.builder()
        .country("Poska")
        .city("Lublin")
        .postalCode("20-802")
        .street("Niebieska")
        .houseNum("10")
        .flatNum("10")
        .phone("123456789")
        .build();
    }

    private boolean ifAddressEquals(AddressDTO address1, AddressDTO address2){
        if(!address1.getCountry().equals(address2.getCountry())) return false;
        if(!address1.getCity().equals(address2.getCity())) return false;
        if(!address1.getPostalCode().equals(address2.getPostalCode())) return false;
        if(!address1.getStreet().equals(address2.getStreet())) return false;
        if(!address1.getHouseNum().equals(address2.getHouseNum())) return false;
        if(!address1.getFlatNum().equals(address2.getFlatNum())) return false;
        if(!address1.getPhone().equals(address2.getPhone())) return false;

        return true;
    }

    @Test
    void getNonExistingAddressByIdTest() throws NotFoundException{
        Long id = 1L;
        assertThrows(NotFoundException.class, () -> {
            service.getSingleAddress(id);
        });
    }

    @Test
    void getExistingAddressByIdTest() throws NotFoundException{
        AddressDTO address = BuildExampleAdress();
        AddressDTO createdAdress = service.createAddress(address);
        service.deleteAddress(createdAdress.getId());
        assertTrue(ifAddressEquals(address, createdAdress));
    }

    @Test
    void CreateFindAndDeleteAddressTest() throws NotFoundException{
        AddressDTO address = BuildExampleAdress();
        AddressDTO createdAdress = service.createAddress(address);
        AddressDTO foundedAdress = service.getSingleAddress(createdAdress.getId());
        service.deleteAddress(createdAdress.getId());
        assertTrue(ifAddressEquals(address, createdAdress));
        assertTrue(ifAddressEquals(address, foundedAdress));
    }

    @Test
    void getDeletedAddressTest() throws NotFoundException{
        AddressDTO address = BuildExampleAdress();
        AddressDTO createdAdress = service.createAddress(address);
        service.deleteAddress(createdAdress.getId());
        assertThrows(NotFoundException.class, () -> {
            service.getSingleAddress(createdAdress.getId());
        });
    }

    @Test
    void CreateAddressWithNullFieldsTest() throws ValidationException, NotFoundException{
        AddressDTO address = AddressDTO.builder().
        AddressDTO createdAdress;
        assertThrows(ValidationException.class, () -> {
            createdAdress = service.createAddress(address);
        });
        assertThrows(NotFoundException.class, () -> {
            service.deleteAddress(createdAdress.getId());
        });
    }

}
