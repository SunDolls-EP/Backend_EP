package com.sundolls.epbackend.mapper.impl;

import com.sundolls.epbackend.dto.response.QuestionResponse;
import com.sundolls.epbackend.entity.Question;
import com.sundolls.epbackend.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper extends EntityMapper<QuestionResponse, Question> {
    QuestionMapper MAPPER = Mappers.getMapper(QuestionMapper.class);
}
