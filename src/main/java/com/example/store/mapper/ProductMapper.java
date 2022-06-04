package com.example.store.mapper;

import com.example.store.dto.product.ProductDTO;
import com.example.store.dto.product.ProductExDTO;
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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "warehouse", source = "warehouse")
    @Mapping(target = "unitsInStock", constant = "0")
    @Mapping(target = "unitsInOrder", constant = "0")
    @Mapping(target = "name", source = "dto.name")
    ProductEntity createEntity(UpdateProductDTO dto, WarehouseEntity warehouse);

    ProductEntity updateEntity(UpdateProductDTO dto, @MappingTarget ProductEntity productEntity);

    ProductDTO toDTO(ProductEntity entity);

    ProductExDTO toExDTO(ProductEntity entity);
}
