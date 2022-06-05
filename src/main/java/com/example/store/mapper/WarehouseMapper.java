package com.example.store.mapper;

import java.time.LocalDate;

import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WarehouseMapper {

    static WarehouseMapper INSTANCE = Mappers.getMapper(WarehouseMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "modificationOperator", ignore = true)
    WarehouseEntity create(String name, AddressEntity address, LocalDate creationDate);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "modificationOperator", source = "operator")
    @Mapping(target = "modificationDate", source = "date")
    WarehouseEntity update(@MappingTarget WarehouseEntity entity, String name, 
                           UserEntity operator, LocalDate date);

    @Mapping(target = "addressId", source = "entity.address.id")
    @Mapping(target = "modificationOperatorId", source = "entity.modificationOperator.id")
    WarehouseDTO toDTO(WarehouseEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "active", constant = "false")
    @Mapping(target = "modificationOperator", source = "operator")
    @Mapping(target = "modificationDate", source = "date")
    WarehouseEntity delete(@MappingTarget WarehouseEntity entity, UserEntity operator, LocalDate date);
}
