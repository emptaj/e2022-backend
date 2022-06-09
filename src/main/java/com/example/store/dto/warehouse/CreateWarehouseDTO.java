package com.example.store.dto.warehouse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.store.dto.address.CreateAddressDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWarehouseDTO {
    @NotNull(message = "Name should not be empty")
    private String name;
    @Valid
    @NotNull
    private CreateAddressDTO address;
}
