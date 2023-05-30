package com.sundolls.epbackend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class StudyInfoResponse {
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Time totalTime;
}
