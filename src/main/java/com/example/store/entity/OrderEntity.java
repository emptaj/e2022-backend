package com.example.store.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.store.entity.enums.OrderState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated
    private OrderState state;
    
    @ManyToOne
    private UserEntity buyer;
    private Date orderDate;
    
    @ManyToOne
    private WarehouseUserEntity modificationOperator;
    private Date modificationDate;

    @ManyToOne
    private WarehouseEntity warehouse;

    @ManyToOne
    private AddressEntity address;

    @ManyToOne
    private DeliveryTypeEntity deliveryType;
}
