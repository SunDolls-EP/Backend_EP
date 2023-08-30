package com.sundolls.epbackend.service;

import com.sundolls.epbackend.dto.request.CalendarRequest;
import com.sundolls.epbackend.dto.response.CalendarResponse;
import com.sundolls.epbackend.entity.Calendar;
import com.sundolls.epbackend.entity.User;
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
    private final UserRepository userRepository;
    private final CalendarMapper calendarMapper;

    public ResponseEntity<List<CalendarResponse>> getCalendarList(Jws<Claims> payload, LocalDateTime from, LocalDateTime to){
        HttpStatus status = HttpStatus.OK;

        User user  = getUser(payload);

        ArrayList<Calendar> calendarArrayList = calendarRepository.findByCreatedAtBetweenAndUser(from, to, user);
        List<CalendarResponse> body = calendarArrayList.stream().map(calendarMapper::toDto).toList();

        return new ResponseEntity<>(body, status);
    }

    public ResponseEntity<CalendarResponse> writeCalendar(Jws<Claims> payload, CalendarRequest request){
        HttpStatus status = HttpStatus.OK;

        User user = getUser(payload);

        LocalDateTime startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        LocalDateTime endAt = LocalDateTime.of(LocalDate.now(),LocalTime.of(23,59,59));

        if (calendarRepository.existsByUserAndCreatedAtBetween(user, startAt, endAt)){
            status = HttpStatus.CONFLICT;
            return new ResponseEntity<>(status);
        }

        Calendar calendar = Calendar.builder()
                .user(user)
                .content(request.getContent())
                .build();
        calendarRepository.save(calendar);

        CalendarResponse body = calendarMapper.toDto(calendar);


        return new ResponseEntity<>(body, status);
    }

    @Transactional
    public ResponseEntity<CalendarResponse> updateCalendar(Jws<Claims> payload, CalendarRequest request){
        HttpStatus status = HttpStatus.OK;

        User user = getUser(payload);

        LocalDateTime startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        LocalDateTime endAt = LocalDateTime.of(LocalDate.now(),LocalTime.of(23,59,59));
        Optional<Calendar> optionalCalendar = calendarRepository.findByUserAndCreatedAtBetween(user,startAt, endAt);
        if (optionalCalendar.isEmpty()) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        }
        Calendar calendar = optionalCalendar.get();
        calendar.update(request.getContent());

        CalendarResponse body = calendarMapper.toDto(calendar);

        return new ResponseEntity<>(body, status);
    }

    private User getUser(Jws<Claims> payload){
        Optional<User> optionalUser = userRepository.findByUsernameAndTag((String) payload.getBody().get("username"), (String) payload.getBody().get("tag"));
        if (optionalUser.isEmpty()) {
            return null;
        }
        return optionalUser.get();
    }
}
