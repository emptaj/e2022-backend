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
public class CreateOrderDetailsDTO {
    private Long productId;
    @NotNull(message = "Quantity should not be empty")
    @Positive(message = "Quantity should be gretter than zero")
    private Integer quantity;
}
