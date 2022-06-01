package com.example.store.dto.warehouse;

import com.example.store.dto.address.AddressDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWarehouseDTO {
    private String name;
    private AddressDTO address;
}
