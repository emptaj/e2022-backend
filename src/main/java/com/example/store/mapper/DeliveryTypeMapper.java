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
import com.example.store.entity.UserEntity;


@Mapper
public interface DeliveryTypeMapper {

    static DeliveryTypeMapper INSTANCE = Mappers.getMapper(DeliveryTypeMapper.class);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "modificationOperator", ignore = true)
    DeliveryTypeEntity create(CreateDeliveryTypeDTO dto, AddressEntity address);

    @Mapping(target = "addressId", source = "address.id")
    DeliveryTypeDTO toDTO(DeliveryTypeEntity entity);

    @Mapping(target = "addressId", source = "address.id")
    @Mapping(target = "modificationOperatorId", source = "modificationOperator.id")
    DeliveryTypeExDTO toExDTO(DeliveryTypeEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "false")
    @Mapping(target = "modificationDate", source = "date")
    @Mapping(target = "modificationOperator", source = "operator")
    DeliveryTypeEntity delete(@MappingTarget DeliveryTypeEntity entity, UserEntity operator, LocalDate date);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "modificationDate", source = "date")
    @Mapping(target = "modificationOperator", source = "operator")
    DeliveryTypeEntity update(@MappingTarget DeliveryTypeEntity entity, UpdateDeliveryTypeDTO dto,
                              UserEntity operator, LocalDate date);
}
