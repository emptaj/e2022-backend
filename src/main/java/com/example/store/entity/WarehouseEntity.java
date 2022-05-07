package com.example.store.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
    private Date creationDate;
    
    @OneToOne
    private AddressEntity address;

    private Boolean active;
    private Date modificationDate;
}
