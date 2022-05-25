package com.example.store.mapper;

import com.example.store.dto.user.RegistrationTokenDTO;
import com.example.store.entity.RegistrationTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegistrationTokenMapper {
    RegistrationTokenMapper INSTANCE = Mappers.getMapper(RegistrationTokenMapper.class);
    
    @Mapping(target = "confirmationPath",
            expression = "java(RegistrationTokenDTO.createConfirmationPath(tokenEntity.getToken()))")
    public RegistrationTokenDTO toDTO(RegistrationTokenEntity tokenEntity);
}
