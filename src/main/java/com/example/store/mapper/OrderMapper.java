package com.example.store.mapper;

import java.time.LocalDate;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.example.store.dto.order.OrderDTO;
import com.example.store.dto.order.OrderDetailsDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.entity.DeliveryTypeEntity;
import com.example.store.entity.OrderDetailsEntity;
import com.example.store.entity.OrderEntity;
import com.example.store.entity.ProductEntity;
import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.entity.enums.OrderState;

@Mapper
public interface OrderMapper {
    
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "unitPrice", source = "product.price")
    OrderDetailsEntity create(OrderEntity order, ProductEntity product, Integer quantity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", constant = "NEW")
    @Mapping(target = "modificationOperator", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "address", source = "address")
    OrderEntity create(UserEntity user, LocalDate orderDate, WarehouseEntity warehouse,
                       AddressEntity address, DeliveryTypeEntity deliveryType);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "modificationOperator", source = "operator")
    @Mapping(target = "modificationDate", source = "date")
    OrderEntity changeState(@MappingTarget OrderEntity entity, OrderState state,
                            UserEntity operator, LocalDate date);
                       
    @Mapping(target = "productId", source = "product.id")
    OrderDetailsDTO toDTO(OrderDetailsEntity entity);

    @Mapping(target = "userId", source = "entity.user.id")
    @Mapping(target = "warehouseId", source = "entity.warehouse.id")
    @Mapping(target = "addressId", source = "entity.address.id")
    @Mapping(target = "deliveryTypeId", source = "entity.deliveryType.id")
    @Mapping(target = "operatorId", source = "entity.modificationOperator.id")
    @Mapping(target = "orderDetails", 
             expression = "java(entity.getOrderDetails().stream().map(this::toDTO).collect(java.util.stream.Collectors.toList()))")
    OrderDTO toDTO(OrderEntity entity);
}
