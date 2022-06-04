package com.example.store.Builder;

import com.example.store.dto.address.AddressDTO;
import com.example.store.dto.deliveryType.CreateDeliveryTypeDTO;
import com.example.store.dto.warehouse.CreateWarehouseDTO;

public class ExampleDTOBuilder {

    public static AddressDTO BuildExampleAddress(){
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

    public static CreateDeliveryTypeDTO buildExampleDeliveryTypeDTO(){
        return CreateDeliveryTypeDTO.builder()
        .name("exampleName")
        .email("exampleEmail@example.com")
        .address(BuildExampleAddress())
        .build();
    }

    public static CreateWarehouseDTO buildExampleWarehouseDTO(){
        return new CreateWarehouseDTO("exampleName", BuildExampleAddress());
    }

}