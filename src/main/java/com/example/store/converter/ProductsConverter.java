package com.example.store.converter;

import com.example.store.entity.ProductEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.model.ProductModel;
import com.example.store.service.WarehouseService;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductsConverter {

    private final WarehouseService warehouseService;
    
    
    public ProductEntity create(ProductModel model) {
        ProductEntity entity = ProductEntity.builder()
                .unitsInStock(0)
                .unitsInOrder(0)
                .build();
        
        update(entity, model);
        return entity;        
    }

    public ProductModel toModel(ProductEntity entity) {
        return ProductModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .warehouseId(entity.getWarehouse().getId())
                .price(entity.getPrice())
                .build();
    }

    public void update(ProductEntity entity, ProductModel model) {
        WarehouseEntity warehouse = warehouseService.findWarehouseById(model.getWarehouseId());
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setWarehouse(warehouse);
        entity.setPrice(model.getPrice());
    }
}
