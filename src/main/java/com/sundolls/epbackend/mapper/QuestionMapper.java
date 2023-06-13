package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.QuestionResponse;
import com.sundolls.epbackend.entity.Question;
import com.sundolls.epbackend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper extends EntityMapper<QuestionResponse, Question> {
    QuestionMapper MAPPER = Mappers.getMapper(QuestionMapper.class);

    @Override
    @Mapping(
            source = "user", target = "writerUsername",
            qualifiedByName = "getUsername"
    )
    @Mapping(
            source = "user", target = "writerTag",
            qualifiedByName = "getTag"
    )
    QuestionResponse toDto(final Question entity);

    @Named("getUsername")
    default String getUsername(User user) {
        return user.getUsername();
    }

    @Named("getTag")
    default String getTag(User user) {
        return user.getTag();
    }

}
