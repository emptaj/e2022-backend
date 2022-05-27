package com.example.store.service;

import java.time.LocalDate;

import com.example.store.dto.SingleValueDTO;
import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.mapper.WarehouseMapper;
import com.example.store.repository.WarehouseRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository repository;
    private final AddressService addressService;
    private final WarehouseMapper mapper = WarehouseMapper.INSTANCE;


    public WarehouseEntity findWarehouseById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(WarehouseEntity.class, id));
    }


    public WarehouseDTO createWarehouse(SingleValueDTO<Long> addressId) {
        AddressEntity address = addressService.findAddressById(addressId.getValue());
        WarehouseEntity entity = mapper.create(address, LocalDate.now());
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    
}
