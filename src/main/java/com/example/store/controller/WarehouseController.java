package com.example.store.controller;

import javax.transaction.Transactional;

import com.example.store.dto.SingleValueDTO;
import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.service.WarehouseService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(path = "/api/warehouses")
@CrossOrigin
@Transactional
@RequiredArgsConstructor
public class WarehouseController {
    
    private final WarehouseService service;
    

    @PostMapping("")
    public WarehouseDTO createWarehouse(@RequestBody SingleValueDTO<Long> addressId) {
        return service.createWarehouse(addressId);
    }
}
