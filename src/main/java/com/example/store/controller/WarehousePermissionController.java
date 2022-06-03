package com.example.store.controller;

import com.example.store.entity.WarehousePermissionEntity;
import com.example.store.entity.enums.WarehousePermission;
import com.example.store.service.UserService;
import com.example.store.service.WarehousePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping(path = "/api/warehouses")
@CrossOrigin
@RequiredArgsConstructor
public class WarehousePermissionController {
    private final WarehousePermissionService warehousePermissionService;

    @PreAuthorize("hasAuthority(#warehouseId + ':UPDATE')")
    @PutMapping("/{warehouseId}/permissions/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignPermissionToUser(@PathVariable Long warehouseId,
                                       @PathVariable Long userId,
                                       @RequestBody WarehousePermission warehousePermission) {
        warehousePermissionService.assignPermissionToUser(warehouseId, userId, warehousePermission);

    }
}
