package com.example.store.entity;

import java.time.LocalDate;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @CreationTimestamp
    private LocalDate creationDate;

    @OneToOne
    private AddressEntity address;

    private Boolean active;

    @UpdateTimestamp
    private LocalDate modificationDate;
}
