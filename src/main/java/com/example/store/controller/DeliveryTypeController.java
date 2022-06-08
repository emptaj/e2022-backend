package com.example.store.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.store.dto.ListDTO;
import com.example.store.dto.deliveryType.CreateDeliveryTypeDTO;
import com.example.store.dto.deliveryType.DeliveryTypeDTO;
import com.example.store.dto.deliveryType.DeliveryTypeExDTO;
import com.example.store.dto.deliveryType.UpdateDeliveryTypeDTO;
import com.example.store.service.DeliveryTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/delivery-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true")
public class DeliveryTypeController {
    
    private final DeliveryTypeService service;


    @GetMapping("")
    public ListDTO<DeliveryTypeDTO> getDeliveryTypes(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return service.getActiveDeliveryTypes(page, size);
    }

    @GetMapping("/extended")
    public ListDTO<DeliveryTypeExDTO> getDeliveryTypesEx(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return service.getDeliveryTypesEx(page, size);
    }

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    public DeliveryTypeDTO createDeliveryType(@Valid @RequestBody CreateDeliveryTypeDTO dto) {
        return service.createDeliveryType(dto);
    }

    @DeleteMapping("/{deliveryTypeId}")
    public void deleteDeliveryType(@PathVariable Long deliveryTypeId) {
        service.deleteDeliveryType(deliveryTypeId);
    }

    @PutMapping("/{deliveryTypeId}")
    public DeliveryTypeDTO updateDeliveryType(@PathVariable Long deliveryTypeId,
    @Valid @RequestBody UpdateDeliveryTypeDTO dto) {
        return service.updateDeliveryType(deliveryTypeId, dto);
    }

}
