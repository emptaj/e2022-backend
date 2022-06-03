package com.example.store;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private AddressService serviceAddress;


    @Test
    void getNonExistingDeliveryTypeByIdTest() throws NotFoundException{
        Long id = 1L;
        assertThrows(NotFoundException.class, () -> {
            service.findDeliveryTypeById(id);
        });
    }

    @Test
    void getAddressFromExistingDeliveryTypeTest(){
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);
        AddressDTO addressCreated = serviceAddress.getSingleAddress(createdDeliveryType.getAddressId());
        assertTrue(EqualDTOChecker.ifAddressEquals(deliveryType.getAddress(), addressCreated));
        service.deleteDeliveryType(createdDeliveryType.getId());
    }

    @Test
    void getAddressFromNonExistingDeliveryTypeTest() throws NotFoundException{
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);
        service.deleteDeliveryType(createdDeliveryType.getId());

        assertThrows(NotFoundException.class, () -> {
            serviceAddress.getSingleAddress(createdDeliveryType.getAddressId());
        });
    }

    @Test
    void getExistingDeliveryTypeByIdTest(){
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);
        AddressDTO addressCreated = serviceAddress.getSingleAddress(createdDeliveryType.getAddressId());
        service.deleteDeliveryType(createdDeliveryType.getId());
        assertTrue(EqualDTOChecker.ifDeliveryTypeEqual(deliveryType, createdDeliveryType, addressCreated));
    }

    @Test
    void CreateFindAndDeleteDeliveryTypeTest(){
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);
        DeliveryTypeDTO foundedDeliveryType = DeliveryTypeMapper.INSTANCE.toDTO(service.findDeliveryTypeById(createdDeliveryType.getId()));
        AddressDTO addressCreated = serviceAddress.getSingleAddress(createdDeliveryType.getAddressId());
        AddressDTO addressFounded = serviceAddress.getSingleAddress(foundedDeliveryType.getAddressId());
        service.deleteDeliveryType(createdDeliveryType.getId());

        assertTrue(EqualDTOChecker.ifDeliveryTypeEqual(deliveryType, createdDeliveryType, addressCreated));
        assertTrue(EqualDTOChecker.ifDeliveryTypeEqual(deliveryType, foundedDeliveryType, addressFounded));
    }

    @Test
    void getDeletedDeliveryTypeTest() throws NotFoundException{
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);
        service.deleteDeliveryType(createdDeliveryType.getId());

        assertThrows(NotFoundException.class, () -> {
            service.findDeliveryTypeById(createdDeliveryType.getId());
        });
    }

    @Test
    void createDeliveryTypeWithEmptyName() throws ValidationException{
        CreateDeliveryTypeDTO deliveryType = CreateDeliveryTypeDTO.builder().name("").email("Example@email.com").address(ExampleDTOBuilder.BuildExampleAddress()).build();

        assertThrows(ValidationException.class, () -> {
            service.createDeliveryType(deliveryType);
        });
    }

}
