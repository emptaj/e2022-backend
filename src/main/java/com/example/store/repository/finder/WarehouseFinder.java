package com.example.store.repository.finder;

import org.springframework.stereotype.Component;

import com.example.store.entity.WarehouseEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.repository.WarehouseRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WarehouseFinder {
    
    private final WarehouseRepository repository;


    public WarehouseEntity byId(Long warehouseId) {
        return repository.findById(warehouseId)
                .orElseThrow(() -> new NotFoundException(WarehouseEntity.class, warehouseId));
    }
}
