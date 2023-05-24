package com.sundolls.epbackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionRequest {
    private String title;
    private String content;
}
