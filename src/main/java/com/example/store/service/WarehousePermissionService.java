package com.example.store.service;

import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.entity.WarehousePermissionEntity;
import com.example.store.entity.enums.WarehousePermission;
import com.example.store.exception.NotFoundException;
import com.example.store.repository.UserRepository;
import com.example.store.repository.WarehousePermissionRepository;
import com.example.store.repository.WarehouseRepository;
import com.example.store.repository.finder.RecordFinder;
import com.example.store.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehousePermissionService {

    private final WarehousePermissionRepository warehousePermissionRepository;

    private final RecordFinder<WarehouseEntity, WarehouseRepository> warehouseFinder;

    private final UserRepository userRepository;
    private final RecordFinder<UserEntity, UserRepository> userFinder;


    @Transactional
    public List<WarehousePermissionEntity> createPermissions(WarehouseEntity warehouseEntity) {

        List<WarehousePermissionEntity> warehousePermissionEntityList =
                Arrays.stream(WarehousePermission.values()).map(perm -> WarehousePermissionEntity.builder()
                                .warehouse(warehouseEntity)
                                .users(new ArrayList<UserEntity>())
                                .name(String.format("%d:%s", warehouseEntity.getId(), perm))
                                .build())
                        .collect(Collectors.toList());

        return warehousePermissionRepository.saveAll(warehousePermissionEntityList);
    }

    @Transactional
    public void assignPermissionToWarehouse(Long warehouseId, Long userId, WarehousePermission permission) {
        warehouseFinder.byId(warehouseId);
        UserEntity user = userFinder.byId(userId);
        String permissionName = String.format("%d:%s", warehouseId, permission.name());
        WarehousePermissionEntity permissionEntity = getPermissionEntityByName(permissionName);
        assignPermission(user, permissionEntity);
    }

    @Transactional
    public void assignAllPermissions(UserEntity user, Collection<WarehousePermissionEntity> permissionEntities) {
        permissionEntities.forEach((permission) -> permission.getUsers().add(user));
        user.getWarehousePermissions().addAll(permissionEntities);
        userRepository.save(user);
    }

    public void assignPermission(UserEntity user, WarehousePermissionEntity permissionEntity) {
        user.getWarehousePermissions().add(permissionEntity);

    }

    public WarehousePermissionEntity getPermissionEntityByName(String permissionName) {
        return warehousePermissionRepository.findByName(permissionName).orElseThrow(
                () -> new NotFoundException(WarehousePermissionEntity.class, permissionName)
        );
    }

    @Transactional
    public void removePermission(UserEntity user, WarehousePermissionEntity permission) {
        user.getWarehousePermissions().remove(permission);
    }

    @Transactional
    public void removeAllPermissions(UserEntity user, Collection<WarehousePermissionEntity> permissionEntities) {
        user.getWarehousePermissions().removeAll(permissionEntities);
    }

    @Transactional
    public void removePermissionToWarehouse(Long warehouseId, Long userId, WarehousePermission warehousePermission) {
        WarehouseEntity warehouseById = warehouseFinder.byId(warehouseId);
        Validator.positiveValue(warehouseById.getActive(), "Cannot edit deleted warehouse");
        UserEntity user = userFinder.byId(userId);
        String permissionName = String.format("%d:%s", warehouseId, warehousePermission.name());
        WarehousePermissionEntity permissionEntity = getPermissionEntityByName(permissionName);
        removePermission(user, permissionEntity);
    }

    public List<WarehousePermissionEntity> getAll() {
        return warehousePermissionRepository.findAll();
    }

    public List<WarehousePermissionEntity> getForWarehouse(Long warehouseId) {
        warehouseFinder.byId(warehouseId);
        return warehousePermissionRepository.findAllByWarehouseId(warehouseId);
    }

    @Transactional
    public void deletePermissionForWarehouse(Long warehouseId) {
        List<WarehousePermissionEntity> forWarehousePermissions = getForWarehouse(warehouseId);

        forWarehousePermissions.forEach(permissionEntity -> {
            List<UserEntity> users = permissionEntity.getUsers();
            users.forEach(userEntity -> removePermission(userEntity, permissionEntity));
        });

        warehousePermissionRepository.deleteAll(forWarehousePermissions);
    }

    public List<SimpleGrantedAuthority> createAuthorities(List<WarehousePermissionEntity> permissionEntities) {
        return permissionEntities.stream().
                map(permissionEntity -> new SimpleGrantedAuthority(permissionEntity.getName()
                ))
                .collect(Collectors.toList());
    }

    public boolean hasPermission(UserEntity user, WarehouseEntity warehouse, WarehousePermission permission) {
        return user.getWarehousePermissions().stream()
                .filter((p) -> {
                    return p.getWarehouse().getId() == warehouse.getId() &&
                            p.getName().equals(warehouse.getId() + ":" + permission.name());
                })
                .findFirst()
                .isPresent();

    }
}
