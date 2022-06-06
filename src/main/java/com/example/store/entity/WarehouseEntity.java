package com.example.store.entity;

import java.time.LocalDate;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "Warehouses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
