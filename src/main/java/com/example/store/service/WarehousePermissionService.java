package com.example.store.service;

import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseUserEntity;
import com.example.store.entity.enums.WarehouseRole;
import com.example.store.repository.WarehouseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehousePermissionService {
    private final UserService userService;
    private final WarehouseUserRepository warehouseUserRepository;

    public Boolean hasAccess(Long warehouseId, Collection<WarehouseRole> roles) {
        UserEntity user = (UserEntity) userService.getLoggedUser();

        List<WarehouseUserEntity> byWarehouseAndUserAAndRoleIn = warehouseUserRepository.findByWarehouseIdAndUserIdAndRoleIn(
                warehouseId,
                user.getId(),
                roles);
        return !byWarehouseAndUserAAndRoleIn.isEmpty();
    }

    public Boolean hasAccess(Long warehouseId, WarehouseRole role) {
        UserEntity user = (UserEntity) userService.getLoggedUser();

        Optional<WarehouseUserEntity> byWarehouseAndUser = warehouseUserRepository.findByWarehouseIdAndUserIdAndRole(
                warehouseId,
                user.getId(),
                role);
        return byWarehouseAndUser.isPresent();

    }
}
