package com.example.store;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.store.Builder.ExampleDTOBuilder;
import com.example.store.EqualChecker.EqualDTOChecker;
import com.example.store.dto.address.AddressDTO;
import com.example.store.dto.address.CreateAddressDTO;
import com.example.store.dto.deliveryType.CreateDeliveryTypeDTO;
import com.example.store.dto.deliveryType.DeliveryTypeDTO;
import com.example.store.exception.NotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.mapper.DeliveryTypeMapper;
import com.example.store.service.AddressService;
import com.example.store.service.DeliveryTypeService;
import com.example.store.validator.Validator;

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
        AddressDTO addressCreated = addressService.getAddress(createdDeliveryType.getAddressId());
        assertTrue(EqualDTOChecker.ifAddressEquals(deliveryType.getAddress(), addressCreated));
    }


    @Test
    @Transactional
    void getExistingDeliveryTypeByIdTest(){
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);
        AddressDTO addressCreated = addressService.getAddress(createdDeliveryType.getAddressId());
        assertTrue(EqualDTOChecker.ifDeliveryTypeEqual(deliveryType, createdDeliveryType, addressCreated));
    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void CreateFindAndDeleteDeliveryTypeTest(){
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);
        DeliveryTypeDTO foundedDeliveryType = DeliveryTypeMapper.INSTANCE.toDTO(service.findDeliveryTypeById(createdDeliveryType.getId()));
        AddressDTO addressCreated = addressService.getAddress(createdDeliveryType.getAddressId());
        AddressDTO addressFounded = addressService.getAddress(foundedDeliveryType.getAddressId());
        service.deleteDeliveryType(createdDeliveryType.getId());

        assertTrue(EqualDTOChecker.ifDeliveryTypeEqual(deliveryType, createdDeliveryType, addressCreated));
        assertTrue(EqualDTOChecker.ifDeliveryTypeEqual(deliveryType, foundedDeliveryType, addressFounded));
    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
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
    void DeleteDeliveryTypeWithoutLoginTest() throws NotFoundException{
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        DeliveryTypeDTO createdDeliveryType = service.createDeliveryType(deliveryType);
        assertThrows(ValidationException.class, () -> {
            service.deleteDeliveryType(createdDeliveryType.getId());
        });
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
            Validator.validate(deliveryType);
        });
    }

    @Test
    @Transactional
    void CheckIfOneItemIsAddedTest() throws NotFoundException{
        CreateDeliveryTypeDTO deliveryType = ExampleDTOBuilder.buildExampleDeliveryTypeDTO();
        service.createDeliveryType(deliveryType);

        assertTrue(service.getActiveDeliveryTypes(0, 100).getItems().size()==1);
    }

    @Test
    @Transactional
    void CreateInvalidEmailTest() throws ValidationException{
        CreateDeliveryTypeDTO deliveryType = CreateDeliveryTypeDTO.builder()
        .name("exampleName")
        .email("email")
        .address(ExampleDTOBuilder.BuildExampleAddress())
        .build();


       

        assertThrows(ValidationException.class, () -> {
            Validator.validate(deliveryType);
        });

    }

    @Test
    @Transactional
    void CreateInvalidNullNameTest() throws ValidationException{
        CreateDeliveryTypeDTO deliveryType = CreateDeliveryTypeDTO.builder()
        .email("email")
        .address(ExampleDTOBuilder.BuildExampleAddress())
        .build();


       

        assertThrows(ValidationException.class, () -> {
            Validator.validate(deliveryType);
        });

    }

    @Test
    @Transactional
    void CreateInvalidBadAddressTest() throws ValidationException{
        CreateDeliveryTypeDTO deliveryType = CreateDeliveryTypeDTO.builder()
        .email("email")
        .address(CreateAddressDTO.builder()
        .country("Polska")
        .postalCode("20-802")
        .street("Niebieska")
        .houseNum("11")
        .flatNum("12")
        .phone("123456789")
        .build())
        .build();


       

        assertThrows(ValidationException.class, () -> {
            Validator.validate(deliveryType);
        });

    }

}
