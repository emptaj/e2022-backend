package com.example.store.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.example.store.dto.ListDTO;
import com.example.store.dto.warehouse.CreateWarehouseDTO;
import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.service.WarehouseService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/api/warehouses", "/api/v1/warehouses" })
@Transactional
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService service;

    @GetMapping("/{warehouseId}")
    public WarehouseDTO getWarehouse(@PathVariable Long warehouseId) {
        return service.getWarehouse(warehouseId);
    }

    @GetMapping
    public ListDTO<WarehouseDTO> getWarehouses(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        return service.getWarehouses(page, size);
    }

    @PostMapping("")
    public WarehouseDTO createWarehouse(@Valid @RequestBody CreateWarehouseDTO dto) {
        return service.createWarehouse(dto);
    }

    @PreAuthorize("hasAuthority(#warehouseId + ':DELETE')")
    @DeleteMapping("/{warehouseId}")
    public void deleteWarehouse(@PathVariable Long warehouseId) {
        service.deleteWarehouse(warehouseId);
    }

    @PreAuthorize("hasAuthority(#warehouseId + ':UPDATE')")
    @PutMapping("/{warehouseId}")
    public WarehouseDTO updateWarehouse(@PathVariable Long warehouseId,
                                        @RequestBody CreateWarehouseDTO dto) {
        return service.updateWarehouse(warehouseId, dto);
    }
}
