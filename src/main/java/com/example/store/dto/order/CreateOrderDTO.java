package com.example.store.dto.order;

import java.util.List;

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
    private CreateAddressDTO address;
    private Long deliveryTypeId;
    private List<CreateOrderDetailsDTO> orderDetails;
}
