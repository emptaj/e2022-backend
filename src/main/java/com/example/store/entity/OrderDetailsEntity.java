package com.example.store.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="OrderDetails")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsEntity {
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    private ProductEntity product;

    @ManyToOne
    private OrderEntity order;

    private Integer quantity;
    private Float unitPrice;
}
