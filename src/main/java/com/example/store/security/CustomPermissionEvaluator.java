package com.example.store.security;

import com.example.store.entity.UserEntity;
import com.example.store.entity.enums.WarehouseRole;
import com.example.store.service.WarehousePermissionService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@RequiredArgsConstructor
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final WarehousePermissionService warehousePermissionService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
//        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
//            return false;
//        }
//        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();
//
//        return hasPrivilege(auth, targetType, permission.toString().toUpperCase());
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        return warehousePermissionService.hasAccess((Long) targetId, (WarehouseRole) permission);
    }


}
