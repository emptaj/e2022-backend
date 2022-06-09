package com.example.store.entity;

import javax.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Table(name = "WarehousePermissions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WarehousePermissionEntity {
    @Id
    @SequenceGenerator(name = "sequence_warehouse_permission_id", sequenceName = "sequence_warehouse_permission_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_warehouse_permission_id")
    private Long id;

    private String name;

    @ManyToOne
    private WarehouseEntity warehouse;

    @ManyToMany(mappedBy = "warehousePermissions")
    private List<UserEntity> users;
}
