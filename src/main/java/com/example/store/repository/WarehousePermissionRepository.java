package com.example.store.repository;

import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehousePermissionEntity;
import com.example.store.entity.enums.WarehouseRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarehousePermissionRepository extends JpaRepository<WarehousePermissionEntity, Long> {

}
