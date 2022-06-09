package com.example.store.dto.deliveryType;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    @NotNull(message = "Name should not be empty")
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @NotNull(message = "Email should not be empty")
    @Email(message = "This is not correct email format")
    private String email;
    @NotNull(message = "Address should not be empty")
    @Valid
    private CreateAddressDTO address;
}
