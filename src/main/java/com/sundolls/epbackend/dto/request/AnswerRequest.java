package com.sundolls.epbackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;

@Getter
@Setter
public class AnswerRequest {
    @Schema(description = "내용", defaultValue = "내용")
    private String content;
}
