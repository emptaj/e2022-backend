package com.example.store.service;

import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.entity.WarehousePermissionEntity;
import com.example.store.repository.WarehousePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehousePermissionService {
    private final WarehousePermissionRepository warehousePermissionRepository;
    public static final ArrayList<String> PERMS = new ArrayList<>(List.of("WRITE", "READ"));

    @Transactional
    public List<WarehousePermissionEntity> createPermissions(WarehouseEntity warehouseEntity) {

        List<WarehousePermissionEntity> warehousePermissionEntityList = PERMS.stream()
                .map(perm -> WarehousePermissionEntity.builder()
                        .warehouse(warehouseEntity)
                        .name(String.format("%d:%s", warehouseEntity.getId(), perm))
                        .build())
                .collect(Collectors.toList());

        warehousePermissionRepository.saveAll(warehousePermissionEntityList);
        return warehousePermissionEntityList;
    }

    @Transactional
    public void assignAllPermissions(UserEntity user, Collection<WarehousePermissionEntity> permissionEntities) {
        user.getWarehousePermissions().addAll(permissionEntities);

    }

    @Transactional
    public void assignPermission(UserEntity user, WarehousePermissionEntity permissionEntity) {
        user.getWarehousePermissions().add(permissionEntity);
    }
}
