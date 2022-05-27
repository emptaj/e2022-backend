package com.example.store.service;

import com.example.store.dto.address.AddressDTO;
import com.example.store.dto.address.UpdateAddressDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.mapper.AddressMapper;
import com.example.store.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper = AddressMapper.INSTANCE;

    public AddressEntity findAddressById(Long addressId) {
        return addressRepository.findById(addressId).
                orElseThrow(() -> new NotFoundException(AddressEntity.class, addressId));
    }

    public List<AddressDTO> getAddresses(int pageNum, int pageSize) {
        return addressRepository.findAll(PageRequest.of(pageNum, pageSize)).
                stream()
                .map(addressMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AddressDTO getSingleAddress(Long addressId) {
        AddressEntity addressEntity = findAddressById(addressId);
        return addressMapper.toDTO(addressEntity);
    }

    public AddressDTO createAddress(AddressDTO addressDTO) {
        AddressEntity saved = addressRepository.save(addressMapper.toEntity(addressDTO));
        return addressMapper.toDTO(saved);
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        AddressEntity addressEntity = findAddressById(addressId);
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
