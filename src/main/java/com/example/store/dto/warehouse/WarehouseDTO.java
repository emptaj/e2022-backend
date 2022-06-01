package com.example.store.dto.warehouse;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDTO {
    private Long id;
    private String name;
    private LocalDate creationDate;
    private Long addressId;
    private Boolean active;
    private LocalDate modificationDate;
}
