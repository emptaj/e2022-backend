package com.example.store.controller;

import com.example.store.model.address.AddressDTO;
import com.example.store.service.AddressService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/")
    public List<AddressDTO> getAddresses() {
        return addressService.getAddresses();
    }

    @GetMapping("/{id}")
    public AddressDTO getSingleAddress(@PathVariable Long addressId) {
        return addressService.getSingleAddress(addressId);
    }


}
