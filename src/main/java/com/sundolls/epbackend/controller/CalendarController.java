package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.request.CalendarRequest;
import com.sundolls.epbackend.dto.response.CalendarResponse;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;
    private final JwtProvider jwtProvider;

    @GetMapping("")
    @Operation(summary = "캘린더 가져오기")
    @Parameters({
            @Parameter(name = "from", description = "yyyy-mm-dd HH 형식으로 조회할 시작일 (기본값 2000-01-01 00)", required = true),
            @Parameter(name = "to", description = "yyyy-mm-dd HH 형식으로 조회할 종료일 (기본값 3000-12-31 23)", required = true)
    })

    public ResponseEntity<List<CalendarResponse>> getCalendar(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "2000-01-01 00") String from,
            @RequestParam(defaultValue = "3000-12-31 23") String to){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

        return calendarService.getCalendarList(user, LocalDateTime.parse(from, formatter), LocalDateTime.parse(to, formatter));

    }


    @PostMapping("")
    @Operation(summary = "캘린더 작성하기")
    public ResponseEntity<CalendarResponse> writeCalendar(
            @AuthenticationPrincipal User user,
            @RequestBody CalendarRequest request) {

        return calendarService.writeCalendar(user, request);

    }


    @PutMapping("")
    @Operation(summary = "캘린더 수정하기")
    public ResponseEntity<CalendarResponse> updateCalendar(
            @AuthenticationPrincipal User user,
            @RequestBody CalendarRequest request){
        return calendarService.updateCalendar(user, request);


    }
}
