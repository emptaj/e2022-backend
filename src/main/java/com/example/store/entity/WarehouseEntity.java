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
    @SequenceGenerator(name = "sequence_warehouse_id", sequenceName = "sequence_warehouse_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_warehouse_id")
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
