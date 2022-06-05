package com.example.store.entity;

import java.time.LocalDate;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Warehouses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    private LocalDate creationDate;

    @ManyToOne
    private AddressEntity address;

    private Boolean active;
    private LocalDate modificationDate;
    
    @ManyToOne
    private UserEntity modificationOperator;
}
