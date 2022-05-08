package com.example.store.service;

import com.example.store.entity.WarehouseEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.repository.WarehouseRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository repository;


    public WarehouseEntity findWarehouseById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(WarehouseEntity.class, id));
    }
}
