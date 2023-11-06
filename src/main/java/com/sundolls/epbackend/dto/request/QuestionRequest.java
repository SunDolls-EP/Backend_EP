package com.sundolls.epbackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionRequest {
    @Schema(description = "제목", defaultValue = "제목")
    private String title;

    @Schema(description = "내용", defaultValue = "내용")
    private String content;
}
