package com.example.store.dto.order;

import java.util.List;

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
public class CreateOrderDTO {
    @NotNull(message = "Address should not be empty")
    private CreateAddressDTO address;
    @NotNull(message = "DeliveryType id should not be empty")
    private Long deliveryTypeId;
    @NotNull(message = "List should not be null")
    @NotEmpty(message = "List with orderDetails should not be empty")
    private List<CreateOrderDetailsDTO> orderDetails;
}
