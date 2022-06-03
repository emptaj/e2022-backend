package com.example.store.repository;

import com.example.store.entity.WarehousePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehousePermissionRepository extends JpaRepository<WarehousePermissionEntity, Long> {

    Optional<WarehousePermissionEntity> findByName(String permissionName);

    List<WarehousePermissionEntity> findAllByWarehouseId(Long warehouseId);
}
