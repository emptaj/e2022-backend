package com.example.store.repository;

import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseUserEntity;
import com.example.store.entity.enums.WarehouseRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseUserRepository extends JpaRepository<WarehouseUserEntity, Long> {
    List<WarehouseUserEntity> findByWarehouseAndUserAAndRoleIn(Long warehouseId,
                                                               UserEntity userId,
                                                               Collection<WarehouseRole> roles);

    Optional<WarehouseUserEntity> findByWarehouseAndUserAAndRole(Long warehouseId,
                                                                 UserEntity userId,
                                                                 WarehouseRole role);
}
