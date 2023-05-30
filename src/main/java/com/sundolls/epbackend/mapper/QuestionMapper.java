package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.QuestionResponse;
import com.sundolls.epbackend.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper extends EntityMapper<QuestionResponse, Question> {
    QuestionMapper MAPPER = Mappers.getMapper(QuestionMapper.class);


}
