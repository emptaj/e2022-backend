package com.example.store.dto.order;

import java.time.LocalDate;
import java.util.List;

import com.example.store.entity.enums.OrderState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private Long warehouseId;

    private LocalDate orderDate;
    private OrderState state;
    private LocalDate modificationDate;

    private Long addressId;
    private Long operatorId;
    private Long deliveryTypeId;

    private List<OrderDetailsDTO> orderDetails;

    private String payuRedirectURL;
}
