package com.example.store.Builder;

import com.example.store.dto.address.CreateAddressDTO;
import com.example.store.dto.deliveryType.CreateDeliveryTypeDTO;
import com.example.store.dto.product.UpdateProductDTO;
import com.example.store.dto.user.CreateUserDTO;
import com.example.store.dto.warehouse.CreateWarehouseDTO;

public class ExampleDTOBuilder {

    public static CreateAddressDTO BuildExampleAddress(){
        return CreateAddressDTO.builder()
        .country("Polska")
        .city("Lublin")
        .postalCode("20-802")
        .street("Niebieska")
        .houseNum("11")
        .flatNum("12")
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

    public static CreateUserDTO buildExampleUserDTO(){
        return new CreateUserDTO("ExampleUser", "ExamplePassword", "Example@email.com");
    }

    public static UpdateProductDTO buildExampleProductDTO(){
        return UpdateProductDTO.builder()
                .name("Produkt")
                .description("Produkt testowy")
                .price(1.11F)
                .build();
    }
    public static UpdateProductDTO buildNoNameProductDTO(){
        return UpdateProductDTO.builder()
                .name("")
                .description("Produkt testowy")
                .price(1F)
                .build();
    }
    public static UpdateProductDTO buildNoDescriptionProductDTO(){
        return UpdateProductDTO.builder()
                .name("Produkt")
                .price(1F)
                .build();
    }
    public static UpdateProductDTO buildNoPriceProductDTO(){
        return UpdateProductDTO.builder()
                .name("")
                .description("Produkt testowy")
                .price(null)
                .build();
    }
    public static UpdateProductDTO buildNegativeStockProductDTO(){
        return UpdateProductDTO.builder()
                .name("Produkt")
                .description("Produkt testowy")
                .price(-1F)
                .build();
    }


}
