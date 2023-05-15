package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.request.CalendarRequest;
import com.sundolls.epbackend.dto.response.CalendarResponse;
import com.sundolls.epbackend.entity.Calendar;
import com.sundolls.epbackend.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("")
    public ResponseEntity<Map> getCalendar(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam Timestamp from, @RequestParam Timestamp to){
        HttpStatus httpStatus = null;
        Map<String, Object> body = new HashMap<>();
        ArrayList<Calendar> calendarArrayList = calendarService.getCalendarList(accessToken, from, to);

        if (calendarArrayList != null) {
            httpStatus = HttpStatus.OK;
            body.put("Calendars",calendarArrayList);
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(body,httpStatus);
    }


    @PostMapping("")
    public ResponseEntity<CalendarResponse> writeCalendar(
            @RequestHeader("Authorization") String accessToken,
            CalendarRequest request) {
        HttpStatus httpStatus = null;
        CalendarResponse body = new CalendarResponse();

        Calendar calendar = calendarService.writeCalendar(accessToken, request);

        if (calendar!=null) {
            httpStatus = HttpStatus.OK;
            body.setContent(calendar.getContent());
            body.setCreatedAt(calendar.getCreatedAt());
            body.setModifiedAt(calendar.getModifiedAt());
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(body, httpStatus);
    }


    @PutMapping("")
    public ResponseEntity<CalendarResponse> updateCalendar(
            @RequestHeader("Authorization") String accessToken,
            CalendarRequest request){
        HttpStatus httpStatus = null;
        CalendarResponse body = new CalendarResponse();

        Calendar calendar = calendarService.updateCalendar(accessToken, request);

        if (calendar!=null) {
            httpStatus = HttpStatus.OK;
            body.setContent(calendar.getContent());
            body.setCreatedAt(calendar.getCreatedAt());
            body.setModifiedAt(calendar.getModifiedAt());
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(body, httpStatus);

    }
}
