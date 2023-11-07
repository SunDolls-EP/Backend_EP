package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.CalendarResponse;
import com.sundolls.epbackend.entity.Calendar;

public class CalendarMapper {

    public static CalendarResponse toDto(Calendar entity) {
        CalendarResponse calendarResponse = new CalendarResponse();
        calendarResponse.setContent(entity.getContent());
        calendarResponse.setCreatedAt(entity.getCreatedAt());
        calendarResponse.setModifiedAt(entity.getModifiedAt());

        return calendarResponse;
    }
}
