package com.sundolls.epbackend.repository;

import com.sundolls.epbackend.entity.Calendar;
import com.sundolls.epbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    ArrayList<Calendar> findByCreatedAtBetweenAndUser (Timestamp from, Timestamp to, User user);

    Optional<Calendar> findByUserAndCreatedAtBetween(User user, Timestamp startAt, Timestamp endAt);
}
