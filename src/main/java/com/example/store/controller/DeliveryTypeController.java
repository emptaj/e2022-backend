package com.example.store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.store.dto.ListDTO;
import com.example.store.dto.deliveryType.CreateDeliveryTypeDTO;
import com.example.store.dto.deliveryType.DeliveryTypeDTO;
import com.example.store.service.DeliveryTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/delivery-types")
@RequiredArgsConstructor
public class DeliveryTypeController {
    
    private final DeliveryTypeService service;


    @GetMapping("")
    public ListDTO<DeliveryTypeDTO> getDeliveryTypes(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return service.getActiveDeliveryTypes(page, size);
    }

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    public DeliveryTypeDTO createDeliveryType(@RequestBody CreateDeliveryTypeDTO dto) {
        return service.createDeliveryType(dto);
    }

    @DeleteMapping("/{deliveryTypeId}")
    public void deleteDeliveryType(@PathVariable Long deliveryTypeId) {
        service.deleteDeliveryType(deliveryTypeId);
    }

}
