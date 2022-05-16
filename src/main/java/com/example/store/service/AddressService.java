package com.example.store.service;

import com.example.store.converter.AddressMapper;
import com.example.store.entity.AddressEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.model.address.AddressDTO;
import com.example.store.model.address.UpdateAddressDTO;
import com.example.store.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper = AddressMapper.INSTANCE;

    public List<AddressDTO> getAddresses() {
        return addressRepository.findAll().stream()
                .map(entity -> addressMapper.toDTO(entity))
                .collect(Collectors.toList());
    }

    public AddressDTO getSingleAddress(Long addressId) {
        AddressEntity addressEntity = addressRepository.findById(addressId).
                orElseThrow(() -> new NotFoundException(AddressEntity.class, addressId));
        return addressMapper.toDTO(addressEntity);
    }

    public AddressDTO createAddress(AddressDTO addressDTO) {
        AddressEntity saved = addressRepository.save(addressMapper.toEntity(addressDTO));
        return addressMapper.toDTO(saved);
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        AddressEntity addressEntity = addressRepository.findById(addressId).
                orElseThrow(() -> new NotFoundException(AddressEntity.class, addressId));
        addressRepository.delete(addressEntity);
    }

    @Transactional
    public AddressDTO updateAddress(Long addressId, UpdateAddressDTO updateAddressDTO) {
        AddressEntity addressEntity = addressRepository.findById(addressId).
                orElseThrow(() -> new NotFoundException(AddressEntity.class, addressId));

        addressEntity = addressMapper.toEntity(updateAddressDTO, addressEntity);
        return addressMapper.toDTO(addressEntity);

    }
}
