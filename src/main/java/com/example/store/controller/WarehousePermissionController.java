package com.example.store.controller;

import com.example.store.entity.WarehousePermissionEntity;
import com.example.store.entity.enums.WarehousePermission;
import com.example.store.service.WarehousePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/warehouses/")
@CrossOrigin
@RequiredArgsConstructor
public class WarehousePermissionController {
    private final WarehousePermissionService warehousePermissionService;

    @PreAuthorize("hasAuthority(#warehouseId + ':UPDATE')")
    @PutMapping("/{warehouseId}/permissions/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignPermissionToWarehouse(@PathVariable Long warehouseId,
                                            @PathVariable Long userId,
                                            @RequestBody WarehousePermission warehousePermission) {
        warehousePermissionService.assignPermissionToWarehouse(warehouseId, userId, warehousePermission);

    }

    @PreAuthorize("hasAuthority(#warehouseId + ':UPDATE')")
    @DeleteMapping("/{warehouseId}/permissions/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePermissionToUser(@PathVariable Long warehouseId,
                                       @PathVariable Long userId,
                                       @RequestBody WarehousePermission warehousePermission) {
        warehousePermissionService.removePermissionToWarehouse(warehouseId, userId, warehousePermission);

    }

    @GetMapping("/")
    public List<WarehousePermissionEntity> getPermisions() {
        return warehousePermissionService.getAll();
    }


    @GetMapping("/{warehouseId}/permissions")
    public List<WarehousePermissionEntity> getPermisionsForWarehouse(@PathVariable Long warehouseId) {
        return warehousePermissionService.getForWarehouse(warehouseId);
    }


}
