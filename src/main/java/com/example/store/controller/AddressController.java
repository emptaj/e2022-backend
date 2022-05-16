package com.example.store.controller;

import com.example.store.model.address.AddressDTO;
import com.example.store.model.address.UpdateAddressDTO;
import com.example.store.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/addresses")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/")
    public List<AddressDTO> getAddresses(@RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "20") int size) {
        return addressService.getAddresses(page, size);
    }

    @GetMapping("/{addressId}")
    public AddressDTO getSingleAddress(@PathVariable Long addressId) {
        return addressService.getSingleAddress(addressId);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDTO createAddress(@RequestBody AddressDTO addressDTO) {
        return addressService.createAddress(addressDTO);
    }

    @DeleteMapping("/{addressId}")
    public void deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
    }

    @PutMapping("/{addressId}")
    public AddressDTO updateAddress(@PathVariable Long addressId,
                                    @RequestBody UpdateAddressDTO updateAddressDTO) {
        return addressService.updateAddress(addressId, updateAddressDTO);
    }

}
