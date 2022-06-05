package com.example.store.Builder;

import com.example.store.dto.address.AddressDTO;
import com.example.store.dto.deliveryType.CreateDeliveryTypeDTO;
import com.example.store.dto.product.ProductDTO;
import com.example.store.dto.user.CreateUserDTO;
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

    public static CreateUserDTO buildExampleUserDTO(){
        return new CreateUserDTO("ExampleName", "ExamplePassword", "Example@email.com");
    }

    public static ProductDTO buildExampleProductDTO(){
        return ProductDTO.builder()
                .name("Produkt")
                .description("Produkt testowy")
                .price(1.11)
                .unitsInStock(1)
                .build();
    }
    public static ProductDTO buildNoNameProductDTO(){
        return ProductDTO.builder()
                .name("")
                .description("Produkt testowy")
                .price(1.11)
                .unitsInStock(1)
                .build();
    }
    public static ProductDTO buildNoDescriptionProductDTO(){
        return ProductDTO.builder()
                .name("Produkt")
                .description("")
                .price(1.11)
                .unitsInStock(1)
                .build();
    }
    public static ProductDTO buildNoPriceProductDTO(){
        return ProductDTO.builder()
                .name("")
                .description("Produkt testowy")
                .unitsInStock(1)
                .price(null)
                .build();
    }
    public static ProductDTO buildNegativeStockProductDTO(){
        return ProductDTO.builder()
                .name("Produkt")
                .description("Produkt testowy")
                .unitsInStock(-1)
                .price(1)
                .build()
    }


}
