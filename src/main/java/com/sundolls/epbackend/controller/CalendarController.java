package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.request.CalendarRequest;
import com.sundolls.epbackend.dto.response.CalendarResponse;
import com.sundolls.epbackend.entity.Calendar;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;
    private final JwtProvider jwtProvider;

    @GetMapping("")
    public ResponseEntity<ArrayList<CalendarResponse>> getCalendar(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam(defaultValue = "2000-01-01 00") String from, @RequestParam(defaultValue = "3000-12-31 23") String to){
        HttpStatus httpStatus = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");


        ArrayList<CalendarResponse> body = calendarService.getCalendarList(jwtProvider.getPayload(accessToken), LocalDateTime.parse(from, formatter), LocalDateTime.parse(to, formatter));

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
            @RequestBody CalendarRequest request) {
        HttpStatus httpStatus = null;

        CalendarResponse body = calendarService.writeCalendar(jwtProvider.getPayload(accessToken), request);

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
            @RequestBody CalendarRequest request){
        HttpStatus httpStatus = null;

        CalendarResponse body = calendarService.updateCalendar(jwtProvider.getPayload(accessToken), request);

        if (body!=null) {
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(body, httpStatus);

    }
}
