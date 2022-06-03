package com.example.store.repository;

import com.example.store.entity.WarehousePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehousePermissionRepository extends JpaRepository<WarehousePermissionEntity, Long> {

}
