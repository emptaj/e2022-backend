package com.example.store.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductExDTO {
    private Long id;
    private Boolean active;
    private String name;
    private String description;
    private Float price;
    private Integer unitsInStock;
    private Integer unitsInOrder;
}
