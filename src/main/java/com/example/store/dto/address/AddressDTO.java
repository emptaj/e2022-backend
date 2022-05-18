package com.example.store.dto.address;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Builder
@Data
public class AddressDTO {
    private Long id;
    private String country;
    private String city;
    private String postalCode;
    private String street;
    private String houseNum;
    private String flatNum;
}
