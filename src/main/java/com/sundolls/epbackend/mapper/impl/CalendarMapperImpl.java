package com.sundolls.epbackend.mapper.impl;

import com.sundolls.epbackend.dto.response.CalendarResponse;
import com.sundolls.epbackend.entity.Calendar;
import com.sundolls.epbackend.mapper.CalendarMapper;
import org.springframework.stereotype.Component;

@Component
public class CalendarMapperImpl implements CalendarMapper {
    @Override
    public CalendarResponse toDto(Calendar entity) {
        CalendarResponse calendarResponse = new CalendarResponse();
        calendarResponse.setContent(entity.getContent());
        calendarResponse.setCreatedAt(entity.getCreatedAt());
        calendarResponse.setModifiedAt(entity.getModifiedAt());

        return calendarResponse;
    }
}
