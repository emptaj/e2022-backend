package com.example.store.dto.address;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UpdateAddressDTO {
    private String country;
    private String city;
    private String postalCode;
    private String street;
    private String houseNum;
    private String flatNum;
    private String phone;
}
