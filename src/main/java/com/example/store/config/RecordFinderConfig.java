package com.example.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.store.entity.AddressEntity;
import com.example.store.entity.DeliveryTypeEntity;
import com.example.store.entity.OrderEntity;
import com.example.store.entity.ProductEntity;
import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.repository.AddressRepository;
import com.example.store.repository.DeliveryTypeRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.UserRepository;
import com.example.store.repository.WarehouseRepository;
import com.example.store.repository.finder.RecordFinder;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RecordFinderConfig {

    private final WarehouseRepository warehouseRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    
    @Bean
    public RecordFinder<WarehouseEntity, WarehouseRepository> createWarehouseFinder() {
        return new RecordFinder<>(WarehouseEntity.class, warehouseRepository);
    }

    @Bean
    public RecordFinder<OrderEntity, OrderRepository> createOrderFinder() {
        return new RecordFinder<>(OrderEntity.class, orderRepository);
    }

    @Bean 
    public RecordFinder<AddressEntity, AddressRepository> createAddressFinder() {
        return new RecordFinder<>(AddressEntity.class, addressRepository);
    }

    @Bean
    public RecordFinder<DeliveryTypeEntity, DeliveryTypeRepository> createDeliveryFinder() {
        return new RecordFinder<>(DeliveryTypeEntity.class, deliveryTypeRepository);
    }

    @Bean
    public RecordFinder<ProductEntity, ProductRepository> createProductFinder() {
        return new RecordFinder<>(ProductEntity.class, productRepository);
    }

    @Bean
    public RecordFinder<UserEntity, UserRepository> createUserFinder() {
        return new RecordFinder<>(UserEntity.class, userRepository);
    }
}
