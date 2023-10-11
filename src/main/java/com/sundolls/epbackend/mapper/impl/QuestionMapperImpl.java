package com.sundolls.epbackend.mapper.impl;

import com.sundolls.epbackend.dto.response.QuestionResponse;
import com.sundolls.epbackend.entity.Question;
import com.sundolls.epbackend.mapper.QuestionMapper;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapperImpl implements QuestionMapper {
    @Override
    public QuestionResponse toDto(Question entity) {
        QuestionResponse questionResponse = new QuestionResponse();
        questionResponse.setId(entity.getId());
        questionResponse.setTitle(entity.getTitle());
        questionResponse.setContent(entity.getContent());
        questionResponse.setWriterUsername(entity.getUser().getUsername());
        questionResponse.setWriterTag(entity.getUser().getTag());
        questionResponse.setCreatedAt(entity.getCreatedAt());
        questionResponse.setModifiedAt(entity.getModifiedAt());
        return questionResponse;
    }
}
