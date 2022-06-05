package com.example.store.mapper;

import java.time.LocalDate;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.example.store.dto.deliveryType.DeliveryTypeDTO;
import com.example.store.dto.deliveryType.DeliveryTypeExDTO;
import com.example.store.dto.deliveryType.UpdateDeliveryTypeDTO;
import com.example.store.dto.deliveryType.CreateDeliveryTypeDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.entity.DeliveryTypeEntity;


@Mapper
public interface DeliveryTypeMapper {

    static DeliveryTypeMapper INSTANCE = Mappers.getMapper(DeliveryTypeMapper.class);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "address", source = "address")
    DeliveryTypeEntity create(CreateDeliveryTypeDTO dto, AddressEntity address);

    @Mapping(target = "addressId", source = "address.id")
    DeliveryTypeDTO toDTO(DeliveryTypeEntity entity);

    @Mapping(target = "addressId", source = "address.id")
    DeliveryTypeExDTO toExDTO(DeliveryTypeEntity entity);

    @Mapping(target = "active", constant = "false")
    DeliveryTypeEntity delete(@MappingTarget DeliveryTypeEntity entity, LocalDate modificationDate);

    @Mapping(target = "address", ignore = true)
    DeliveryTypeEntity update(@MappingTarget DeliveryTypeEntity entity, UpdateDeliveryTypeDTO dto);
}
