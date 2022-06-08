package com.example.store;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.store.Builder.ExampleDTOBuilder;
import com.example.store.EqualChecker.EqualDTOChecker;
import com.example.store.dto.address.AddressDTO;
import com.example.store.dto.warehouse.CreateWarehouseDTO;
import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.entity.WarehouseEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.service.AddressService;
import com.example.store.service.WarehouseService;

@SpringBootTest
@AutoConfigureMockMvc
class WarehouseServiceTests {
    

    @Autowired
    private WarehouseService service;
    @Autowired
    private AddressService addressService;

    
    @Test
    @Transactional
    void getNonExistingWarehouseByIdTest() throws NotFoundException{
        Long id = -1L;

        assertThrows(NotFoundException.class, () -> {
            service.getWarehouse(id);
        });
    }

    @Test
    @Transactional
    @WithMockUser
    void createdWarehouseTest(){
        CreateWarehouseDTO warehouse = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO createdWarehouse = service.createWarehouse(warehouse);
        AddressDTO address = addressService.getAddress(createdWarehouse.getAddressId());
        
        assertTrue(EqualDTOChecker.ifWarehouseEqual(warehouse, createdWarehouse, address));
    }

    @Test
    @Transactional
    void createdWarehouseWithoutLoginTest(){
        assertThrows(ValidationException.class, () -> {
            CreateWarehouseDTO warehouse = ExampleDTOBuilder.buildExampleWarehouseDTO();
            service.createWarehouse(warehouse);
        });
    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void findAddressFromCreatedWarehouseTest(){
        CreateWarehouseDTO warehouse = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO createdWarehouse = service.createWarehouse(warehouse);
        AddressDTO address = addressService.getAddress(createdWarehouse.getAddressId());

        assertTrue(EqualDTOChecker.ifAddressEquals(warehouse.getAddress(), address));
    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void findCreatedWarehouseTest(){
        CreateWarehouseDTO warehouse = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO createdWarehouse = service.createWarehouse(warehouse);
        WarehouseDTO foundedWarehouse = service.getWarehouse(createdWarehouse.getId());
        AddressDTO foundedAddress = addressService.getAddress(foundedWarehouse.getAddressId());
        AddressDTO createdAddress = addressService.getAddress(createdWarehouse.getAddressId());

        assertTrue(EqualDTOChecker.ifWarehouseEqual(warehouse, createdWarehouse, createdAddress));
        assertTrue(EqualDTOChecker.ifWarehouseEqual(warehouse, foundedWarehouse, foundedAddress));
    }

    @Test
    @Transactional
    void createWarehouseWithEmptyNameTest() throws ValidationException{
        CreateWarehouseDTO warehouse = new CreateWarehouseDTO("", ExampleDTOBuilder.BuildExampleAddress());

        assertThrows(ValidationException.class, () -> {
            service.createWarehouse(warehouse);
        });
    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void findUnactivedWarehouseTest(){
        CreateWarehouseDTO warehouse = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO createdWarehouse = service.createWarehouse(warehouse);
        service.deleteWarehouse(createdWarehouse.getId());

        assertThrows(NotFoundException.class, () -> {
            service.getWarehouses(0, 100)
            .getItems()
            .stream()
            .filter(deletedWarehouse -> deletedWarehouse.getId() == createdWarehouse.getId())
            .findFirst().orElseThrow(() -> new NotFoundException(WarehouseEntity.class, createdWarehouse.getId()));
        });
    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void findActiveWarehouseTest(){
        CreateWarehouseDTO warehouse = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO createdWarehouse = service.createWarehouse(warehouse);

        assertTrue(!service.getWarehouses(0, 100).
        getItems()
        .stream()
        .filter(deletedWarehouse -> deletedWarehouse.getId() == createdWarehouse.getId())
        .collect(Collectors.toList())
        .isEmpty());

        service.deleteWarehouse(createdWarehouse.getId());
    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void checkModificationDateTest(){
        CreateWarehouseDTO warehouse = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO createdWarehouse = service.createWarehouse(warehouse);
        service.deleteWarehouse(createdWarehouse.getId());
        WarehouseDTO deletedWarehouse = service.getWarehouse(createdWarehouse.getId());
        assertTrue(deletedWarehouse.getModificationDate().equals(LocalDate.now()));
    }
}
