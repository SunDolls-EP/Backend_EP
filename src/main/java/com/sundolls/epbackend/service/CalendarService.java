package com.sundolls.epbackend.service;

import com.sundolls.epbackend.dto.request.CalendarRequest;
import com.sundolls.epbackend.dto.response.CalendarResponse;
import com.sundolls.epbackend.entity.Calendar;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.repository.CalendarRepository;
import com.sundolls.epbackend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public ArrayList<CalendarResponse> getCalendarList(Jws<Claims> payload, LocalDateTime from, LocalDateTime to){
        Optional<User> optionalUser = userRepository.findByUsernameAndTag((String) payload.getBody().get("username"), (String) payload.getBody().get("tag"));
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user  = optionalUser.get();
        ArrayList<Calendar> calendarArrayList = calendarRepository.findByCreatedAtBetweenAndUser(from, to, user);

        ArrayList<CalendarResponse> calendarResponseArrayList = new ArrayList<>();
        for (Calendar calendar : calendarArrayList) {
            CalendarResponse element = new CalendarResponse();
            setCalendarResponse(element, calendar);
            calendarResponseArrayList.add(element);
        }

        return calendarResponseArrayList;
    }

    public CalendarResponse writeCalendar(Jws<Claims> payload, CalendarRequest request){
        Optional<User> optionalUser = userRepository.findByUsernameAndTag((String) payload.getBody().get("username"), (String) payload.getBody().get("tag"));
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user  = optionalUser.get();

        Calendar calendar = Calendar.builder()
                .user(user)
                .content(request.getContent())
                .build();
        calendarRepository.save(calendar);

        CalendarResponse calendarResponse = new CalendarResponse();
        setCalendarResponse(calendarResponse, calendar);

        return calendarResponse;
    }

    @Transactional
    public CalendarResponse updateCalendar(Jws<Claims> payload, CalendarRequest request){
        Optional<User> optionalUser = userRepository.findByUsernameAndTag((String) payload.getBody().get("username"), (String) payload.getBody().get("tag"));
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user  = optionalUser.get();
        LocalDateTime startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        LocalDateTime endAt = LocalDateTime.of(LocalDate.now(),LocalTime.of(23,59,59));
        Optional<Calendar> optionalCalendar = calendarRepository.findByUserAndCreatedAtBetween(user,startAt, endAt);
        if (optionalCalendar.isEmpty()) {
            return null;
        }
        Calendar calendar = optionalCalendar.get();
        calendar.update(request.getContent());

        CalendarResponse calendarResponse = new CalendarResponse();
        setCalendarResponse(calendarResponse, calendar);

        return calendarResponse;
    }

    private void setCalendarResponse(CalendarResponse calendarResponse, Calendar calendar){
        calendarResponse.setContent(calendar.getContent());
        calendarResponse.setCreatedAt(calendar.getCreatedAt());
        calendarResponse.setModifiedAt(calendar.getModifiedAt());
    }
}
