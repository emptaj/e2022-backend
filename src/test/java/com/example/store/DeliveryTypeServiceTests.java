package com.example.store;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.store.Builder.ExampleDTOBuilder;
import com.example.store.EqualChecker.EqualDTOChecker;
import com.example.store.dto.address.AddressDTO;
import com.example.store.dto.deliveryType.CreateDeliveryTypeDTO;
import com.example.store.dto.deliveryType.DeliveryTypeDTO;
import com.example.store.exception.NotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.mapper.DeliveryTypeMapper;
import com.example.store.service.AddressService;
import com.example.store.service.DeliveryTypeService;

@SpringBootTest
class DeliveryTypeServiceTests {
    
    @Autowired
    private DeliveryTypeService service;
    @Autowired
    private AddressService addressService;


    @Test
    @Transactional
    void getNonExistingDeliveryTypeByIdTest() throws NotFoundException{
        Long id = -1L;
        assertThrows(NotFoundException.class, () -> {
            service.findDeliveryTypeById(id);
        });
    }

    @Test
    @Transactional
    void getAddressFromExistingDeliveryTypeTest(){
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);
        AddressDTO addressCreated = addressService.getSingleAddress(createdDeliveryType.getAddressId());
        assertTrue(EqualDTOChecker.ifAddressEquals(deliveryType.getAddress(), addressCreated));
    }

    @Test
    @Transactional
    void getAddressUnactiveDeliveryTypeTest() throws NotFoundException{
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);
        service.deleteDeliveryType(createdDeliveryType.getId());

        assertTrue(addressService.getAddresses(0, 100)
        .stream()
        .filter(address -> address.getId() == createdDeliveryType.getAddressId())
        .collect(Collectors.toList())
        .isEmpty());
    }

    @Test
    @Transactional
    void getExistingDeliveryTypeByIdTest(){
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);
        AddressDTO addressCreated = addressService.getSingleAddress(createdDeliveryType.getAddressId());
        assertTrue(EqualDTOChecker.ifDeliveryTypeEqual(deliveryType, createdDeliveryType, addressCreated));
    }

    @Test
    @Transactional
    void CreateFindAndDeleteDeliveryTypeTest(){
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);
        DeliveryTypeDTO foundedDeliveryType = DeliveryTypeMapper.INSTANCE.toDTO(service.findDeliveryTypeById(createdDeliveryType.getId()));
        AddressDTO addressCreated = addressService.getSingleAddress(createdDeliveryType.getAddressId());
        AddressDTO addressFounded = addressService.getSingleAddress(foundedDeliveryType.getAddressId());
        service.deleteDeliveryType(createdDeliveryType.getId());

        assertTrue(EqualDTOChecker.ifDeliveryTypeEqual(deliveryType, createdDeliveryType, addressCreated));
        assertTrue(EqualDTOChecker.ifDeliveryTypeEqual(deliveryType, foundedDeliveryType, addressFounded));
    }

    @Test
    @Transactional
    void getUnactiveDeliveryTypeTest() throws NotFoundException{
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);
        service.deleteDeliveryType(createdDeliveryType.getId());

        assertTrue(service.getActiveDeliveryTypes(0, 100)
        .getItems()
        .stream()
        .filter(delivery -> delivery.getId() == createdDeliveryType.getId())
        .collect(Collectors.toList())
        .isEmpty());
    }

    @Test
    @Transactional
    void getActiveDeliveryTypeTest() throws NotFoundException{
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);

        assertTrue(!service.getActiveDeliveryTypes(0, 100)
        .getItems()
        .stream()
        .filter(delivery -> delivery.getId() == createdDeliveryType.getId())
        .collect(Collectors.toList())
        .isEmpty());

    }

    @Test
    @Transactional
    void createDeliveryTypeWithEmptyName() throws ValidationException{
        CreateDeliveryTypeDTO deliveryType = CreateDeliveryTypeDTO.builder().name("").email("Example@email.com").address(ExampleDTOBuilder.BuildExampleAddress()).build();

        assertThrows(ValidationException.class, () -> {
            service.createDeliveryType(deliveryType);
        });
    }

}
