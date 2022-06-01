package com.example.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.store.dto.deliveryType.DeliveryTypeDTO;
import com.example.store.entity.DeliveryTypeEntity;


@Mapper
public interface DeliveryTypeMapper {

    static DeliveryTypeMapper INSTANCE = Mappers.getMapper(DeliveryTypeMapper.class);

    @Mapping(target = "addressId", source = "address.id")
    DeliveryTypeDTO toDTO(DeliveryTypeEntity entity);
}
