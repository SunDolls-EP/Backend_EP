package com.sundolls.epbackend.service;

import com.sundolls.epbackend.dto.request.CalendarRequest;
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

    public ArrayList<Calendar> getCalendarList(String accessToken, Timestamp from, Timestamp to){
        Optional<User> optionalUser = userRepository.findById(jwtProvider.getUsername(accessToken));
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user  = optionalUser.get();
        ArrayList<Calendar> calendarArrayList = calendarRepository.findByCreatedAtBetweenAndUser(from, to, user);

        return calendarArrayList;
    }

    public Calendar writeCalendar(String accessToken, CalendarRequest request){
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
        return calendar;
    }

    @Transactional
    public Calendar updateCalendar(String accessToken, CalendarRequest request){
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
        return calendar;
    }
}
