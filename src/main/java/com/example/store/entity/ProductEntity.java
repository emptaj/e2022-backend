package com.example.store.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @SequenceGenerator(name = "sequence_product_id", sequenceName = "sequence_product_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_product_id")
    private Long id;
    private Boolean active;

    private String name;
    private String description;

    @OneToOne
    private WarehouseEntity warehouse;

    private Float price;
    private Integer unitsInStock;
    private Integer unitsInOrder;

    private String imageURL;
}
