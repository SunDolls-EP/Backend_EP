package com.sundolls.epbackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class StudyInfoRequest {
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    @Schema(description = "공부 시작 시간", defaultValue = "2023-12-13T17:17:49")
    private LocalDateTime startAt;

    @Schema(description = "총 공부 시간(초 단위)", defaultValue = "12345")
    private Integer totalStudyTime;
}
