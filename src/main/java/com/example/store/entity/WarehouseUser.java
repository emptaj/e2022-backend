package com.example.store.entity;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.store.entity.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="WarehouseUsers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseUser {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated
    private UserRole role;

    @OneToOne
    private User user;

    @OneToOne
    private Warehouse warehouse;
}
