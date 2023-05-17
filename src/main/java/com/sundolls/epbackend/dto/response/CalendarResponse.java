package com.sundolls.epbackend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class CalendarResponse {
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
