package com.example.store.entity;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.store.entity.enums.UserRole;

import com.example.store.entity.enums.WarehouseRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WarehouseUsers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseUserEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated
    private WarehouseRole role;

    @OneToOne
    private UserEntity user;

    @OneToOne
    private WarehouseEntity warehouse;
}
