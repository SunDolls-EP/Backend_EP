package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.QuestionResponse;
import com.sundolls.epbackend.entity.Question;

public class QuestionMapper {

    public static QuestionResponse toDto(Question entity) {
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
