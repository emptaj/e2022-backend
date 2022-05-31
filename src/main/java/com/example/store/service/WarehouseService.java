package com.example.store.service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.example.store.dto.ListDTO;
import com.example.store.dto.SingleValueDTO;
import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.entity.WarehouseUserEntity;
import com.example.store.entity.enums.WarehouseRole;
import com.example.store.exception.NotFoundException;
import com.example.store.mapper.WarehouseMapper;
import com.example.store.repository.WarehouseRepository;

import com.example.store.repository.WarehouseUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository repository;
    private final WarehouseUserRepository permissionRepository;
    private final AddressService addressService;
    private final UserService userService;
    private final WarehouseMapper mapper = WarehouseMapper.INSTANCE;


    public WarehouseEntity findWarehouseById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(WarehouseEntity.class, id));
    }

    @Transactional
    public WarehouseDTO createWarehouse(SingleValueDTO<Long> addressId) {
        AddressEntity addressEntity = addressService.findAddressById(addressId.getValue());
        WarehouseEntity warehouseEntity = mapper.create(addressEntity, LocalDate.now());
        UserEntity userEntity = (UserEntity) userService.getLoggedUser();

        WarehouseUserEntity permissionEntity = WarehouseUserEntity.builder()
                .warehouse(warehouseEntity)
                .user(userEntity)
                .role(WarehouseRole.OWNER)
                .build();

        repository.save(warehouseEntity);
        permissionRepository.save(permissionEntity);
        return mapper.toDTO(warehouseEntity);
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
        return mapper.toDTO(repository.findById(warehouseId).orElseThrow(
                () -> new NotFoundException(WarehouseEntity.class, warehouseId)));
    }
}
