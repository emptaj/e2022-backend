package com.example.store.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Addresses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {
    @Id
    @SequenceGenerator(name = "sequence_address_id", sequenceName = "sequence_address_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_address_id")
    private Long id;
    private String country;
    private String city;
    private String postalCode;
    private String street;
    private String houseNum;
    private String flatNum;
    private String phone;
}
