package com.example.store.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WarehousePermissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehousePermissionEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private WarehouseEntity warehouse;
}
