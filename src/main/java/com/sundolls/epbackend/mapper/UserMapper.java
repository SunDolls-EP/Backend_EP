package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper extends EntityMapper<UserResponse, User>{
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Override
    UserResponse toDto(final User entity);
}
