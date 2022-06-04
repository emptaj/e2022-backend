package com.example.store;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.store.Builder.ExampleDTOBuilder;
import com.example.store.EqualChecker.EqualDTOChecker;
import com.example.store.dto.address.AddressDTO;
import com.example.store.dto.warehouse.CreateWarehouseDTO;
import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.exception.NotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.service.AddressService;
import com.example.store.service.WarehouseService;

@SpringBootTest
class WarehouseServiceTests {
    

    @Autowired
    private WarehouseService service;
    @Autowired
    private AddressService addressService;

    @Test
    void getNonExistingWarehouseByIdTest() throws NotFoundException{
        Long id = -1L;

        assertThrows(NotFoundException.class, () -> {
            service.getWarehouse(id);
        });
    }

    @Test
    void createdWarehouseTest(){
        CreateWarehouseDTO warehouse = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO createdWarehouse = service.createWarehouse(warehouse);
        AddressDTO address = addressService.getSingleAddress(createdWarehouse.getAddressId());
        
        assertTrue(EqualDTOChecker.ifWarehouseEqual(warehouse, createdWarehouse, address));
    }

    @Test
    void findAddressFromCreatedWarehouseTest(){
        CreateWarehouseDTO warehouse = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO createdWarehouse = service.createWarehouse(warehouse);
        AddressDTO address = addressService.getSingleAddress(createdWarehouse.getAddressId());

        assertTrue(EqualDTOChecker.ifAddressEquals(warehouse.getAddress(), address));
    }

    @Test
    void findCreatedWarehouseTest(){
        CreateWarehouseDTO warehouse = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO createdWarehouse = service.createWarehouse(warehouse);
        WarehouseDTO foundedWarehouse = service.getWarehouse(createdWarehouse.getId());
        AddressDTO createdAddress = addressService.getSingleAddress(createdWarehouse.getAddressId());
        AddressDTO foundedAddress = addressService.getSingleAddress(createdWarehouse.getAddressId());

        assertTrue(EqualDTOChecker.ifWarehouseEqual(warehouse, createdWarehouse, createdAddress));
        assertTrue(EqualDTOChecker.ifWarehouseEqual(warehouse, foundedWarehouse, foundedAddress));
    }

    @Test
    void createWarehouseWithEmptyNameTest() throws ValidationException{
        CreateWarehouseDTO warehouse = new CreateWarehouseDTO("", ExampleDTOBuilder.BuildExampleAddress());

        assertThrows(ValidationException.class, () -> {
            service.createWarehouse(warehouse);
        });
    }

    @Test
    void findUnactivedWarehouseTest(){
        CreateWarehouseDTO warehouse = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO createdWarehouse = service.createWarehouse(warehouse);
        service.deleteWarehouse(createdWarehouse.getId());

        assertTrue(service.getWarehouses(0, 100).
        getItems()
        .stream()
        .filter(deletedWarehouse -> deletedWarehouse.getId() == createdWarehouse.getId())
        .collect(Collectors.toList())
        .isEmpty());
    }

    @Test
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
}
