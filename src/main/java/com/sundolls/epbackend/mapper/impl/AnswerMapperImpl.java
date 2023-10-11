package com.sundolls.epbackend.mapper.impl;

import com.sundolls.epbackend.dto.response.AnswerResponse;
import com.sundolls.epbackend.entity.Answer;
import com.sundolls.epbackend.mapper.AnswerMapper;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapperImpl implements AnswerMapper {
    @Override
     public AnswerResponse toDto(Answer entity) {
        AnswerResponse answerResponse = new AnswerResponse();
        answerResponse.setContent(entity.getContent());
        answerResponse.setWriterName(entity.getUser().getUsername());
        answerResponse.setTag(entity.getUser().getTag());
        answerResponse.setCreatedAt(entity.getCreatedAt());
        answerResponse.setModifiedAt(entity.getModifiedAt());
        return answerResponse;
    }
}
