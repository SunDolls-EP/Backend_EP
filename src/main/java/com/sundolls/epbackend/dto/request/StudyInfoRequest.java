package com.sundolls.epbackend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StudyInfoRequest {
    LocalDateTime startAt;
    LocalDateTime endAt;
}
