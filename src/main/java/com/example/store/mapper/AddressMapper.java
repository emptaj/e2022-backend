package com.example.store.mapper;

import com.example.store.dto.address.AddressDTO;
import com.example.store.dto.address.UpdateAddressDTO;
import com.example.store.entity.AddressEntity;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {

    public static AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    public AddressDTO toDTO(AddressEntity addressEntity);

    public AddressEntity toEntity(AddressDTO addressDTO);

    public AddressEntity toEntity(UpdateAddressDTO addressDTO, @MappingTarget AddressEntity addressEntity);
}
