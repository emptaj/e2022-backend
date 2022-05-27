package com.example.store.mapper;

import java.time.LocalDate;

import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.entity.WarehouseEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WarehouseMapper {

    static WarehouseMapper INSTANCE = Mappers.getMapper(WarehouseMapper.class);

    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", expression = "java(creationDate)")
    @Mapping(target = "address", expression = "java(address)")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "modificationDate", ignore = true)
    WarehouseEntity create(AddressEntity address, LocalDate creationDate);

    @Mapping(target = "addressId", source = "entity.address.id")
    WarehouseDTO toDTO(WarehouseEntity entity);
}
