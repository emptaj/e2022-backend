package com.example.store.entity;

import javax.persistence.*;

import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "WarehousePermissions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WarehousePermissionEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private WarehouseEntity warehouse;

    @ManyToMany(mappedBy = "warehousePermissions")
    private List<UserEntity> users;
}
