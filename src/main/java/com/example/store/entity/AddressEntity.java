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
    @GeneratedValue
    private Long id;
    private String country;
    private String city;
    private String postalCode;
    private String street;
    private String houseNum;
    private String flatNum;
    private String phone;
}
