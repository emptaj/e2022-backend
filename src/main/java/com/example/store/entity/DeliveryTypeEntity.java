package com.example.store.entity;

import java.time.LocalDate;

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
@Table(name = "DeliveryTypes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTypeEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Boolean active;
    private String name;
    private String email;

    @ManyToOne
    private AddressEntity address;

    private LocalDate modificationDate;
}
