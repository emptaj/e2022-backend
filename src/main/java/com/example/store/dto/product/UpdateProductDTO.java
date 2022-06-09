package com.example.store.dto.product;

import javax.validation.constraints.NotEmpty;
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
public class UpdateProductDTO {
    @NotNull(message = "Name id should not be empty")
    @NotEmpty(message = "Postal cod should not be empty")
    private String name;
    @NotNull(message = "Description should not be empty")
    private String description;
    @NotNull(message = "Price should not be empty")
    @Positive(message = "Price should be gretter than zero")
    private Float price;
}
