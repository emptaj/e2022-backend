package com.example.store.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DeliveryTypes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTypeEntity {
    @Id
    @SequenceGenerator(name = "sequence_delivery_type_id", sequenceName = "sequence_delivery_type_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_delivery_type_id")
    private Long id;
    private Boolean active;
    private String name;
    private String email;

    @ManyToOne
    private AddressEntity address;

    private LocalDate modificationDate;
    
    @ManyToOne
    private UserEntity modificationOperator;
}
