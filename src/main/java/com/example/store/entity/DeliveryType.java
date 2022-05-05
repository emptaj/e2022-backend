package com.example.store.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class DeliveryType {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String telephone;
}
