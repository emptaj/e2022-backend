package com.example.store.dto.deliveryType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTypeExDTO {
    private Long id;
    private Boolean active;
    private String name;
    private String email;
    private Long addressId;
}
