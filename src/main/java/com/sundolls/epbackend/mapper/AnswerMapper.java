package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.AnswerResponse;
import com.sundolls.epbackend.entity.Answer;
import com.sundolls.epbackend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnswerMapper extends EntityMapper<AnswerResponse, Answer>{
    AnswerMapper MAPPER = Mappers.getMapper(AnswerMapper.class);

    @Override
    @Mapping(
            source = "user", target = "writerName"
            ,qualifiedByName = "userToUsername"
    )
    AnswerResponse toDto(final Answer entity);

    @Named("userToUsername")
    default String userToUsername(User user) {
        return user.getUsername();
    }
}
