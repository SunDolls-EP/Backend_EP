package com.sundolls.epbackend.repository;

import com.sundolls.epbackend.entity.Calendar;
import com.sundolls.epbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    boolean existsByUserAndCreatedAtBetween(User user, LocalDateTime createdAtStart, LocalDateTime createdAtEnd);

    ArrayList<Calendar> findByCreatedAtBetweenAndUser (LocalDateTime from, LocalDateTime to, User user);

    Optional<Calendar> findByUserAndCreatedAtBetween(User user, LocalDateTime startAt, LocalDateTime endAt);
}
