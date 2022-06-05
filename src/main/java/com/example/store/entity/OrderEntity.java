package com.example.store.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    
    @Enumerated(EnumType.STRING)
    private OrderState state;
    
    @ManyToOne
    private UserEntity user;
    private LocalDate orderDate;
    
    @ManyToOne
    private UserEntity modificationOperator;
    private LocalDate modificationDate;

    @ManyToOne
    private WarehouseEntity warehouse;

    @ManyToOne
    private AddressEntity address;

    @ManyToOne
    private DeliveryTypeEntity deliveryType;

    @OneToMany(mappedBy = "order")
    private List<OrderDetailsEntity> orderDetails;
}
