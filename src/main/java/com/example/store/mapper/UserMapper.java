package com.example.store.mapper;


import com.example.store.dto.user.CreateUserDTO;
import com.example.store.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "locked", constant = "false")
    @Mapping(target = "enabled", constant = "false")
    UserEntity toEntity(CreateUserDTO createUserDTO);
}
