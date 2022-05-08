package com.example.store.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
    private Long id;
    private String name;
    private String description;
    private Long warehouseId;
    private Float price;
}
