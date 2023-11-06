package com.sundolls.epbackend.service;

import com.sundolls.epbackend.dto.request.CalendarRequest;
import com.sundolls.epbackend.dto.response.CalendarResponse;
import com.sundolls.epbackend.entity.Calendar;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.execption.CustomException;
import com.sundolls.epbackend.execption.ErrorCode;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.mapper.CalendarMapper;
import com.sundolls.epbackend.repository.CalendarRepository;
import com.sundolls.epbackend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final CalendarMapper calendarMapper;

    public ResponseEntity<List<CalendarResponse>> getCalendarList(User user, LocalDateTime from, LocalDateTime to){

        ArrayList<Calendar> calendarArrayList = calendarRepository.findByCreatedAtBetweenAndUser(from, to, user);
        List<CalendarResponse> body = calendarArrayList.stream().map(calendarMapper::toDto).toList();

        return ResponseEntity.ok(body);
    }

    public ResponseEntity<CalendarResponse> writeCalendar(User user, CalendarRequest request){

        LocalDateTime startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        LocalDateTime endAt = LocalDateTime.of(LocalDate.now(),LocalTime.of(23,59,59));

        if (calendarRepository.existsByUserAndCreatedAtBetween(user, startAt, endAt)){
            throw new CustomException(ErrorCode.CALENDAR_CONFLICT);
        }

        Calendar calendar = Calendar.builder()
                .user(user)
                .content(request.getContent())
                .build();
        calendarRepository.save(calendar);

        CalendarResponse body = calendarMapper.toDto(calendar);

        return ResponseEntity.ok(body);
    }

    @Transactional
    public ResponseEntity<CalendarResponse> updateCalendar(User user, CalendarRequest request){

        LocalDateTime startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        LocalDateTime endAt = LocalDateTime.of(LocalDate.now(),LocalTime.of(23,59,59));
        Calendar calendar = calendarRepository.findByUserAndCreatedAtBetween(user,startAt, endAt)
                .orElseThrow(() -> new CustomException(ErrorCode.CALENDAR_NOT_FOUND));

        calendar.update(request.getContent());

        CalendarResponse body = calendarMapper.toDto(calendar);

        return ResponseEntity.ok(body);
    }

}
