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
    public ResponseEntity<ArrayList<CalendarResponse>> getCalendar(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam Timestamp from, @RequestParam Timestamp to){
        HttpStatus httpStatus = null;
        ArrayList<CalendarResponse> body = calendarService.getCalendarList(accessToken, from, to);

        if (body != null) {
            httpStatus = HttpStatus.OK;
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

        CalendarResponse body = calendarService.writeCalendar(accessToken, request);

        if (body!=null) {
            httpStatus = HttpStatus.OK;
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

        CalendarResponse body = calendarService.updateCalendar(accessToken, request);

        if (body!=null) {
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(body, httpStatus);

    }
}
