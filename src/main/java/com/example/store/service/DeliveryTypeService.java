package com.example.store.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.store.dto.ListDTO;
import com.example.store.dto.deliveryType.CreateDeliveryTypeDTO;
import com.example.store.dto.deliveryType.DeliveryTypeDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.entity.DeliveryTypeEntity;
import com.example.store.exception.ValidationException;
import com.example.store.mapper.DeliveryTypeMapper;
import com.example.store.repository.DeliveryTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryTypeService {

    private final DeliveryTypeRepository repository;
    private final DeliveryTypeMapper mapper = DeliveryTypeMapper.INSTANCE;

    private final AddressService addressService;


    public ListDTO<DeliveryTypeDTO> getDeliveryTypes(int page, int size) {

        Page<DeliveryTypeEntity> pageResponse = repository.findAll(PageRequest.of(page, size));
        List<DeliveryTypeDTO> deliveryTypes = pageResponse.getContent().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return new ListDTO<>(
            pageResponse.getTotalPages(),
            deliveryTypes
        );
    }


    public DeliveryTypeDTO createDeliveryType(CreateDeliveryTypeDTO dto) {
        validateName(dto.getName());
        AddressEntity address = addressService.createAddressEntity(dto.getAddress());
        DeliveryTypeEntity entity = mapper.create(dto, address);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }
    
    private void validateName(String name) {
        if (!StringUtils.hasText(name))
            throw new ValidationException("Delivery type name cannot be empty");
    }
    
}
