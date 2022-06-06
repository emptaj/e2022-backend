package com.example.store.dto.address;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CreateAddressDTO {
    @NotNull(message = "Country should not be empty")
    @NotEmpty(message = "Country should not be empty")
    private String country;
    @NotNull(message = "City should not be empty")
    @NotEmpty(message = "City should not be empty")
    private String city;
    @NotNull(message = "Postal cod should not be empty")
    @NotEmpty(message = "Postal cod should not be empty")
    private String postalCode;
    @NotNull(message = "Street should not be empty")
    @NotEmpty(message = "Street should not be empty")
    private String street;
    @NotNull(message = "House number should not be empty")
    @NotEmpty(message = "House number should not be empty")
    private String houseNum;
    @NotEmpty(message = "Flat number should not be empty")
    private String flatNum;
    @NotNull(message = "Phone number should not be empty")
    @NotEmpty(message = "Phone number should not be empty")
    private String phone;
}
