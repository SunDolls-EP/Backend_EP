package com.sundolls.epbackend.service;

import com.sundolls.epbackend.dto.request.CalendarRequest;
import com.sundolls.epbackend.dto.response.CalendarResponse;
import com.sundolls.epbackend.entity.Calendar;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.repository.CalendarRepository;
import com.sundolls.epbackend.repository.UserRepository;
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

    public ArrayList<CalendarResponse> getCalendarList(String accessToken, Timestamp from, Timestamp to){
        Optional<User> optionalUser = userRepository.findById(jwtProvider.getUsername(accessToken));
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user  = optionalUser.get();
        ArrayList<Calendar> calendarArrayList = calendarRepository.findByCreatedAtBetweenAndUser(from, to, user);
        ArrayList<CalendarResponse> calendarResponseArrayList = new ArrayList<>();
        for (Calendar calendar : calendarArrayList) {
            CalendarResponse element = new CalendarResponse();
            element.setContent(calendar.getContent());
            element.setCreatedAt(calendar.getCreatedAt());
            element.setModifiedAt(calendar.getModifiedAt());
            calendarResponseArrayList.add(element);
        }

        return calendarResponseArrayList;
    }

    public CalendarResponse writeCalendar(String accessToken, CalendarRequest request){
        Optional<User> optionalUser = userRepository.findById(jwtProvider.getUsername(accessToken));
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
        calendarResponse.setContent(calendar.getContent());
        calendarResponse.setCreatedAt(calendar.getCreatedAt());
        calendarResponse.setModifiedAt(calendar.getModifiedAt());

        return calendarResponse;
    }

    @Transactional
    public CalendarResponse updateCalendar(String accessToken, CalendarRequest request){
        Optional<User> optionalUser = userRepository.findById(jwtProvider.getUsername(accessToken));
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user  = optionalUser.get();
        LocalDateTime startAt = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0,0,0));
        LocalDateTime endAt = LocalDateTime.of(LocalDate.now(),LocalTime.of(23,59,59));
        Optional<Calendar> optionalCalendar = calendarRepository.findByUserAndCreatedAtBetween(user,Timestamp.valueOf(startAt), Timestamp.valueOf(endAt));
        if (optionalCalendar.isEmpty()) {
            return null;
        }
        Calendar calendar = optionalCalendar.get();
        calendar.update(request.getContent());

        CalendarResponse calendarResponse = new CalendarResponse();
        calendarResponse.setContent(calendar.getContent());
        calendarResponse.setCreatedAt(calendar.getCreatedAt());
        calendarResponse.setModifiedAt(calendar.getModifiedAt());
        return calendarResponse;
    }
}
