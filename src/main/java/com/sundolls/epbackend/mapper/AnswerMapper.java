package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.AnswerResponse;
import com.sundolls.epbackend.entity.Answer;

public class AnswerMapper {

     public static AnswerResponse toDto(Answer entity) {
        AnswerResponse answerResponse = new AnswerResponse();
        answerResponse.setContent(entity.getContent());
        answerResponse.setWriterName(entity.getUser().getUsername());
        answerResponse.setTag(entity.getUser().getTag());
        answerResponse.setCreatedAt(entity.getCreatedAt());
        answerResponse.setModifiedAt(entity.getModifiedAt());
        return answerResponse;
    }
}
