package com.example.store.service;

import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.entity.WarehousePermissionEntity;
import com.example.store.entity.enums.WarehousePermission;
import com.example.store.exception.NotFoundException;
import com.example.store.repository.WarehousePermissionRepository;
import com.example.store.repository.WarehouseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehousePermissionService {

    private final WarehousePermissionRepository warehousePermissionRepository;
    private final UserService userService;
    private final WarehouseRepository warehouseRepository;


    private WarehouseEntity findWarehouseById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(WarehouseEntity.class, id));
    }


    @Transactional
    public List<WarehousePermissionEntity> createPermissions(WarehouseEntity warehouseEntity) {

        List<WarehousePermissionEntity> warehousePermissionEntityList =
                Arrays.stream(WarehousePermission.values()).map(perm -> WarehousePermissionEntity.builder()
                                .warehouse(warehouseEntity)
                                .name(String.format("%d:%s", warehouseEntity.getId(), perm))
                                .build())
                        .collect(Collectors.toList());

        warehousePermissionRepository.saveAll(warehousePermissionEntityList);
        return warehousePermissionEntityList;
    }

    @Transactional
    public void assignPermissionToWarehouse(Long warehouseId, Long userId, WarehousePermission permission) {
        findWarehouseById(warehouseId);
        UserEntity user = userService.getUserById(userId);
        String permissionName = String.format("%d:%s", warehouseId, permission.name());
        WarehousePermissionEntity permissionEntity = getPermissionEntityByName(permissionName);
        assignPermission(user, permissionEntity);
    }

    @Transactional
    public void assignAllPermissions(UserEntity user, Collection<WarehousePermissionEntity> permissionEntities) {
        user.getWarehousePermissions().addAll(permissionEntities);
    }

    @Transactional
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
        findWarehouseById(warehouseId);
        UserEntity user = userService.getUserById(userId);
        String permissionName = String.format("%d:%s", warehouseId, warehousePermission.name());
        WarehousePermissionEntity permissionEntity = getPermissionEntityByName(permissionName);
        removePermission(user, permissionEntity);
    }

    public List<WarehousePermissionEntity> getAll() {
        return warehousePermissionRepository.findAll();
    }

    public List<WarehousePermissionEntity> getForWarehouse(Long warehouseId) {
        findWarehouseById(warehouseId);
        return warehousePermissionRepository.findAllByWarehouseId(warehouseId);
    }

    @Transactional
    public void deletePermissionForWarehouse(Long warehouseId) {
        List<WarehousePermissionEntity> forWarehousePermissions = getForWarehouse(warehouseId);

        forWarehousePermissions.forEach(permissionEntity -> {
            List<UserEntity> users = permissionEntity.getUsers();
            users.forEach(userEntity -> removeAllPermissions(userEntity, forWarehousePermissions));
        });

        warehousePermissionRepository.deleteAll(forWarehousePermissions);
    }


}
