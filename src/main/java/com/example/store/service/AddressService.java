package com.example.store.service;

import com.example.store.dto.address.AddressDTO;
import com.example.store.dto.address.UpdateAddressDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.mapper.AddressMapper;
import com.example.store.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper = AddressMapper.INSTANCE;

    public AddressEntity findAddressById(Long addressId) {
        return addressRepository.findById(addressId).
                orElseThrow(() -> new NotFoundException(AddressEntity.class, addressId));
    }

    public AddressDTO getAddress(Long addressId) {
        AddressEntity addressEntity = findAddressById(addressId);
        return addressMapper.toDTO(addressEntity);
    }

    public AddressDTO createAddress(AddressDTO addressDTO) {
        AddressEntity entity = createAddressEntity(addressDTO);
        return addressMapper.toDTO(entity);
    }

    public AddressEntity createAddressEntity(AddressDTO addressDTO) {
        AddressEntity entity = addressMapper.create(addressDTO);
        return addressRepository.save(entity);
    }

    public AddressDTO updateAddress(Long addressId, UpdateAddressDTO updateAddressDTO) {
        AddressEntity addressEntity = findAddressById(addressId);
        addressEntity = updateAddress(addressEntity, updateAddressDTO);
        return addressMapper.toDTO(addressEntity);
    }
    
    public AddressEntity updateAddress(AddressEntity entity, UpdateAddressDTO dto) {
        entity = addressMapper.update(dto, entity);
        return addressRepository.save(entity);
    }
}
