package com.example.store.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

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
    @SequenceGenerator(name = "sequence_order_id", sequenceName = "sequence_order_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_order_id")
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

    @Column(columnDefinition="text", length=10485760)
    private String payuRedirectURL;
}
