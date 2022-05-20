package com.example.store.mapper;

import com.example.store.dto.product.ProductDTO;
import com.example.store.dto.product.UpdateProductDTO;
import com.example.store.entity.ProductEntity;
import com.example.store.entity.WarehouseEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "warehouse", expression = "java(warehouse)")
    @Mapping(target = "id", ignore = true)
    ProductEntity createEntity(UpdateProductDTO dto, WarehouseEntity warehouse);

    ProductEntity updateEntity(UpdateProductDTO dto, @MappingTarget ProductEntity productEntity);

    ProductDTO toDTO(ProductEntity entity);
}
