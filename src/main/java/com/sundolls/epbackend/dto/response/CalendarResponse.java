package com.sundolls.epbackend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CalendarResponse {
    private String content;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
}
