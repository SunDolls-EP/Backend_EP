package com.sundolls.epbackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarRequest {
    @Schema(description = "내용", defaultValue = "내용")
    String content;
}
