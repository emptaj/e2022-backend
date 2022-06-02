package com.example.store.mapper;

import java.time.LocalDate;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

@Mapper
public interface OrderMapper {
    
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "unitPrice", source = "product.price")
    OrderDetailsEntity create(OrderEntity order, ProductEntity product, Integer quantity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", constant = "NEW")
    @Mapping(target = "buyer", expression = "java(buyer)")
    @Mapping(target = "orderDate", expression = "java(orderDate)")
    @Mapping(target = "modificationOperator", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "warehouse", expression = "java(warehouse)")
    @Mapping(target = "address", expression = "java(address)")
    @Mapping(target = "deliveryType", expression = "java(deliveryType)")
    OrderEntity create(UserEntity buyer, LocalDate orderDate, WarehouseEntity warehouse,
                       AddressEntity address, DeliveryTypeEntity deliveryType);
    
    @Mapping(target = "productId", source = "product.id")
    OrderDetailsDTO toDTO(OrderDetailsEntity entity);

    @Mapping(target = "buyerId", source = "entity.buyer.id")
    @Mapping(target = "warehouseId", source = "entity.warehouse.id")
    @Mapping(target = "addressId", source = "entity.address.id")
    @Mapping(target = "deliveryTypeId", source = "entity.deliveryType.id")
    OrderDTO toDTO(OrderEntity entity, List<OrderDetailsDTO> orderDetails);
}
