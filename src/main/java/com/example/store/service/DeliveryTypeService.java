package com.example.store.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.store.dto.ListDTO;
import com.example.store.dto.deliveryType.CreateDeliveryTypeDTO;
import com.example.store.dto.deliveryType.DeliveryTypeDTO;
import com.example.store.dto.deliveryType.DeliveryTypeExDTO;
import com.example.store.dto.deliveryType.UpdateDeliveryTypeDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.entity.DeliveryTypeEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.mapper.DeliveryTypeMapper;
import com.example.store.repository.DeliveryTypeRepository;
import com.example.store.validator.Validator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryTypeService {

    private final DeliveryTypeRepository repository;
    private final DeliveryTypeMapper mapper = DeliveryTypeMapper.INSTANCE;

    private final AddressService addressService;
    private final UserService userService;


    public DeliveryTypeEntity findDeliveryTypeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(DeliveryTypeEntity.class, id));
    }


    public ListDTO<DeliveryTypeDTO> getActiveDeliveryTypes(int page, int size) {

        Page<DeliveryTypeEntity> pageResponse = repository.findAllByActive(true, PageRequest.of(page, size));
        List<DeliveryTypeDTO> deliveryTypes = pageResponse.getContent().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return new ListDTO<>(
            pageResponse.getTotalPages(),
            deliveryTypes
        );
    }


    public ListDTO<DeliveryTypeExDTO> getDeliveryTypesEx(int page, int size) {
        Page<DeliveryTypeEntity> pageResponse = repository.findAll(PageRequest.of(page, size));
        List<DeliveryTypeExDTO> deliveryTypes = pageResponse.getContent().stream()
                .map(mapper::toExDTO)
                .collect(Collectors.toList());

        return new ListDTO<>(
            pageResponse.getTotalPages(),
            deliveryTypes
        );
    }


    public DeliveryTypeDTO createDeliveryType(CreateDeliveryTypeDTO dto) {
        AddressEntity address = addressService.createAddressEntity(dto.getAddress());
        DeliveryTypeEntity entity = mapper.create(dto, address);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }
    

    public void deleteDeliveryType(Long deliveryTypeId) {
        DeliveryTypeEntity entity = findDeliveryTypeById(deliveryTypeId);
        Validator.positiveValue(entity.getActive(), "Delivery type already deleted");
        entity = mapper.delete(entity, userService.getLoggedUserEntity(), LocalDate.now());
        repository.save(entity);
    }


    public DeliveryTypeDTO updateDeliveryType(Long deliveryTypeId, UpdateDeliveryTypeDTO dto) {
        DeliveryTypeEntity deliveryType = findDeliveryTypeById(deliveryTypeId);
        Validator.positiveValue(deliveryType.getActive(), "Cannot edit deleted delivery type");
        addressService.updateAddress(deliveryType.getAddress(), dto.getAddress());
        deliveryType = mapper.update(deliveryType, dto, userService.getLoggedUserEntity(), LocalDate.now());
        deliveryType = repository.save(deliveryType);
        return mapper.toDTO(deliveryType);
    }
}
