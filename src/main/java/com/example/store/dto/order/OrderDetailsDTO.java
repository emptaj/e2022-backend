package com.example.store.dto.order;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDTO {
    private Long id;
    @NotNull(message = "product should not be empty")
    private Long productId;
    @NotNull(message = "Quantity should not be empty")
    @Positive(message = "Quantity should be gretter than zero")
    private Integer quantity;
    @NotNull(message = "Unitprice should not be empty")
    @Positive(message = "Unit price should be gretter than zero")
    private Float unitPrice;
}
