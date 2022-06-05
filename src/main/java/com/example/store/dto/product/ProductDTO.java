package com.example.store.dto.product;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    @NotNull(message = "Name should not be empty")
    private String name;
    @NotNull(message = "Description should not be empty")
    private String description;
    @Positive(message = "Price should be gretter than zero")
    @NotNull(message = "Price should not be empty")
    private Float price;
    @PositiveOrZero(message = "Unit in stock cant be negative")
    @NotNull(message = "Unit in stock should not be empty")
    private Integer unitsInStock;
}
