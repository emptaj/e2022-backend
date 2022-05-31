package com.example.store.controller;

import javax.transaction.Transactional;

import com.example.store.dto.ListDTO;
import com.example.store.dto.SingleValueDTO;
import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.service.WarehouseService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.security.Principal;


@RestController
@RequestMapping(path = "/api/warehouses")
@CrossOrigin
@Transactional
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService service;

    @GetMapping
    public ListDTO<WarehouseDTO> getWarehouses(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        return service.getWarehouses(page, size);
    }

    @PostMapping("")
    public WarehouseDTO createWarehouse(@RequestBody SingleValueDTO<Long> addressId) {
        return service.createWarehouse(addressId);
    }

    @DeleteMapping("/{warehouseId}")
    public void deleteWarehouse(@PathVariable Long warehouseId) {
        service.deleteWarehouse(warehouseId);
    }
}
