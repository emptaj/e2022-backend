package com.example.store.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.example.store.dto.ListDTO;
import com.example.store.dto.SingleValueDTO;
import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.mapper.WarehouseMapper;
import com.example.store.repository.WarehouseRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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


    public void deleteWarehouse(Long warehouseId) {
        WarehouseEntity entity = findWarehouseById(warehouseId);
        mapper.delete(entity, LocalDate.now());
        repository.save(entity);
    }


    public ListDTO<WarehouseDTO> getWarehouses(int page, int size) {
        Page<WarehouseEntity> pageResponse = repository.findAllByActive(true, PageRequest.of(page, size));

        List<WarehouseDTO> warehouses = pageResponse.getContent().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return new ListDTO<>(pageResponse.getTotalPages(), warehouses);
    }

    
}
