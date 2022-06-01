package com.example.store.security;

import com.example.store.entity.UserEntity;
import com.example.store.entity.enums.WarehouseRole;
import com.example.store.service.WarehousePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.AnnotatedType;
import java.security.Principal;

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
        if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }

        System.out.println(permission.getClass());
        System.out.println(permission);
        UserEntity principal = (UserEntity) authentication.getPrincipal();
        return warehousePermissionService.hasAccess(principal, (Long) targetId, WarehouseRole.valueOf(permission.toString()));


    }


}
