package com.example.store.controller;

import com.example.store.dto.address.AddressDTO;
import com.example.store.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping({ "/api/addresses", "/api/v1/addresses" })
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/{addressId}")
    public AddressDTO getSingleAddress(@PathVariable Long addressId) {
        return addressService.getAddress(addressId);
    }

}
