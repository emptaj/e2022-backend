package com.example.store.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.store.dto.order.OrderDTO;
import com.example.store.dto.order.OrderDetailsDTO;
import com.example.store.entity.OrderDetailsEntity;
import com.example.store.entity.OrderEntity;

@Mapper
public interface OrderMapper {
    
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    
    @Mapping(target = "productId", source = "entity.product.id")
    OrderDetailsDTO toDTO(OrderDetailsEntity entity);

    @Mapping(target = "buyerId", source = "entity.buyer.id")
    @Mapping(target = "warehouseId", source = "entity.warehouse.id")
    @Mapping(target = "deliveryTypeId", source = "entity.deliveryType.id")
    @Mapping(target = "orderDetails", expression = "java(orderDetails)")
    OrderDTO toDTO(OrderEntity entity, List<OrderDetailsDTO> orderDetails);
}
