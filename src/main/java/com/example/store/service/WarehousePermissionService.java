package com.example.store.service;

import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseUserEntity;
import com.example.store.entity.enums.WarehouseRole;
import com.example.store.repository.WarehouseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehousePermissionService {
    private final UserService userService;
    private final WarehouseUserRepository warehouseUserRepository;

    public Boolean hasAccess(Long warehouseId, UserEntity user, Collection<WarehouseRole> roles) {
        List<WarehouseUserEntity> byWarehouseAndUserAAndRoleIn = warehouseUserRepository.findByWarehouseAndUserAndRoleIn(
                warehouseId,
                user,
                roles);
        return !byWarehouseAndUserAAndRoleIn.isEmpty();
    }

    public Boolean hasAccess(Long warehouseId, WarehouseRole role) {
        UserEntity user = (UserEntity) userService.getLoggedUser();
        Optional<WarehouseUserEntity> byWarehouseAndUser = warehouseUserRepository.findByWarehouseAndUserAndRole(
                warehouseId,
                user,
                role);
        return byWarehouseAndUser.isPresent();

    }
}
