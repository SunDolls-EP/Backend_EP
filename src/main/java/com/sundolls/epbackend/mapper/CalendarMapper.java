package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.CalendarResponse;
import com.sundolls.epbackend.entity.Calendar;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CalendarMapper extends EntityMapper<CalendarResponse, Calendar>{
    CalendarMapper MAPPER = Mappers.getMapper(CalendarMapper.class);
}
