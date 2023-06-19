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
import org.w3c.dom.stylesheets.LinkStyle;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;
    private final JwtProvider jwtProvider;

    @GetMapping("")
    public ResponseEntity<List<CalendarResponse>> getCalendar(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam(defaultValue = "2000-01-01 00") String from, @RequestParam(defaultValue = "3000-12-31 23") String to){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

        return calendarService.getCalendarList(jwtProvider.getPayload(accessToken), LocalDateTime.parse(from, formatter), LocalDateTime.parse(to, formatter));

    }


    @PostMapping("")
    public ResponseEntity<CalendarResponse> writeCalendar(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody CalendarRequest request) {

        return calendarService.writeCalendar(jwtProvider.getPayload(accessToken), request);

    }


    @PutMapping("")
    public ResponseEntity<CalendarResponse> updateCalendar(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody CalendarRequest request){
        return calendarService.updateCalendar(jwtProvider.getPayload(accessToken), request);


    }
}
