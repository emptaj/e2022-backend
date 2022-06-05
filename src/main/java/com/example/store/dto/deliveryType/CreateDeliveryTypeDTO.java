package com.example.store.dto.deliveryType;

import com.example.store.dto.address.CreateAddressDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeliveryTypeDTO {
    private String name;
    private String email;
    private CreateAddressDTO address;
}
