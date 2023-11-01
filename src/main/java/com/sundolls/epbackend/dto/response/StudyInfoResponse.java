package com.sundolls.epbackend.dto.response;

import com.sundolls.epbackend.entity.StudyInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class StudyInfoResponse {

    @Schema(description = "공부 시작 시간", defaultValue = "2023-12-13T17:17:49")
    private LocalDateTime startAt;

    @Schema(description = "총 공부 시간(초 단위)", defaultValue = "12345")
    private Integer totalTime;
}
