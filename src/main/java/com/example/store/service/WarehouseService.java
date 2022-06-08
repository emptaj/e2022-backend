package com.example.store.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.example.store.dto.ListDTO;
import com.example.store.dto.order.OrderDTO;
import com.example.store.dto.warehouse.CreateWarehouseDTO;
import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.entity.WarehousePermissionEntity;
import com.example.store.exception.ValidationException;
import com.example.store.mapper.WarehouseMapper;
import com.example.store.repository.WarehouseRepository;
import com.example.store.security.AuthoritiesUpdater;
import com.example.store.repository.finder.RecordFinder;
import com.example.store.validator.Validator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository repository;
    private final WarehousePermissionService permissionService;
    private final WarehouseMapper mapper = WarehouseMapper.INSTANCE;
    private final RecordFinder<WarehouseEntity, WarehouseRepository> finder;

    private final AddressService addressService;
    private final OrderService orderService;
    private final UserService userService;
    private final AuthoritiesUpdater authoritiesUpdater;


    @Transactional
    public WarehouseDTO createWarehouse(CreateWarehouseDTO dto) {
        AddressEntity address = addressService.createAddressEntity(dto.getAddress());
        WarehouseEntity warehouse = mapper.create(dto.getName(), address, LocalDate.now());
        warehouse = repository.save(warehouse);
        UserEntity userEntity = userService.getLoggedUserEntity();
        List<WarehousePermissionEntity> permissions = permissionService.createPermissions(warehouse);
        permissionService.assignAllPermissions(userEntity, permissions);
        authoritiesUpdater.update(userEntity);
        return mapper.toDTO(warehouse);
    }

    @Transactional
    public void deleteWarehouse(Long warehouseId) {
        WarehouseEntity entity = finder.byId(warehouseId);
        Validator.positiveValue(entity.getActive(), "Warehouse with given id not found");
        validateNoPendingOrders(entity);
        entity = mapper.delete(entity, userService.getLoggedUserEntity(), LocalDate.now());
        permissionService.deletePermissionForWarehouse(warehouseId);
        repository.save(entity);
    }

    private void validateNoPendingOrders(WarehouseEntity warehouse) {
        ListDTO<OrderDTO> pendingOrders = orderService.getPendingOrders(warehouse, 0, 1);
        if (pendingOrders.getItems().size() > 0)
            throw new ValidationException("Cannot delete warehouse with pending orders");
    }


    public ListDTO<WarehouseDTO> getWarehouses(int page, int size) {
        Page<WarehouseEntity> pageResponse = repository.findAllByActive(true, PageRequest.of(page, size));

        List<WarehouseDTO> warehouses = pageResponse.getContent().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return new ListDTO<>(pageResponse.getTotalPages(), warehouses);
    }


    public WarehouseDTO getWarehouse(Long warehouseId) {
        return mapper.toDTO(finder.byId(warehouseId));
    }

    public WarehouseDTO updateWarehouse(Long warehouseId, CreateWarehouseDTO dto) {
        WarehouseEntity warehouse = finder.byId(warehouseId);
        Validator.positiveValue(warehouse.getActive(), "Cannot edit deleted warehouse");
        addressService.updateAddress(warehouse.getAddress(), dto.getAddress());
        warehouse = mapper.update(warehouse, dto.getName(), userService.getLoggedUserEntity(), LocalDate.now());
        warehouse = repository.save(warehouse);
        return mapper.toDTO(warehouse);
    }
}
