package com.example.store.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.example.store.dto.ListDTO;
import com.example.store.dto.warehouse.CreateWarehouseDTO;
import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.entity.WarehousePermissionEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.mapper.WarehouseMapper;
import com.example.store.repository.WarehouseRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository repository;
    private final WarehousePermissionService permissionService;
    private final AddressService addressService;
    private final UserService userService;
    private final WarehouseMapper mapper = WarehouseMapper.INSTANCE;


    public WarehouseEntity findWarehouseById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(WarehouseEntity.class, id));
    }


    public WarehouseDTO createWarehouse(CreateWarehouseDTO dto) {
        validateName(dto.getName());
        AddressEntity address = addressService.createAddressEntity(dto.getAddress());
        WarehouseEntity entity = mapper.create(dto.getName(), address, LocalDate.now());
        UserEntity userEntity = userService.getLoggedUserEntity();
        entity = repository.save(entity);

        List<WarehousePermissionEntity> permissions = permissionService.createPermissions(entity);
        permissionService.assignAllPermissions(userEntity, permissions);
        return mapper.toDTO(entity);
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name))
            throw new ValidationException("Warehouse name cannot be empty");
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


    public WarehouseDTO getWarehouse(Long warehouseId) {
        return mapper.toDTO(findWarehouseById(warehouseId));
    }


}
