package com.example.store.dto.deliveryType;

import java.time.LocalDate;

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
    private LocalDate modificationDate;
    private Long modificationOperatorId;
}
