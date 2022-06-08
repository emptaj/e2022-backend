package com.example.store.service;

import com.example.store.dto.address.AddressDTO;
import com.example.store.dto.address.CreateAddressDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.mapper.AddressMapper;
import com.example.store.repository.AddressRepository;
import com.example.store.repository.finder.RecordFinder;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper = AddressMapper.INSTANCE;
    private final RecordFinder<AddressEntity, AddressRepository> finder;


    public AddressDTO getAddress(Long addressId) {
        AddressEntity addressEntity = finder.byId(addressId);
        return addressMapper.toDTO(addressEntity);
    }

    public AddressDTO createAddress(CreateAddressDTO addressDTO) {
        AddressEntity entity = createAddressEntity(addressDTO);
        return addressMapper.toDTO(entity);
    }

    public AddressEntity createAddressEntity(CreateAddressDTO addressDTO) {
        AddressEntity entity = addressMapper.create(addressDTO);
        return addressRepository.save(entity);
    }

    public AddressDTO updateAddress(Long addressId, CreateAddressDTO updateAddressDTO) {
        AddressEntity addressEntity = finder.byId(addressId);
        addressEntity = updateAddress(addressEntity, updateAddressDTO);
        return addressMapper.toDTO(addressEntity);
    }

    public AddressEntity updateAddress(AddressEntity entity, CreateAddressDTO dto) {
        entity = addressMapper.update(dto, entity);
        return addressRepository.save(entity);
    }
}
