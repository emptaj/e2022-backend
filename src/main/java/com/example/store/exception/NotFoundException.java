package com.example.store.exception;

import com.example.store.entity.WarehousePermissionEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> clazz, Long id) {
        super(String.format(
                "%s with id = %d not found",
                clazz.getSimpleName().replace("Entity", ""),
                id));
    }

    public NotFoundException(Class<WarehousePermissionEntity> clazz, String permissionName) {
        super(String.format(
                "%s with name = %s not found",
                clazz.getSimpleName().replace("Entity", ""),
                permissionName));
    }
}
