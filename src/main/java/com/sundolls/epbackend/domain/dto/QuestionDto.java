package com.sundolls.epbackend.domain.dto;

import com.sundolls.epbackend.domain.entity.User;
import lombok.Data;

@Data
public class QuestionDto {
    private String title;
    private String content;
    private String userId;
}
