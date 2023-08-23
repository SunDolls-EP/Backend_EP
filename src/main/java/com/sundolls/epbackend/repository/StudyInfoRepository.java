package com.sundolls.epbackend.repository;

import com.sundolls.epbackend.entity.StudyInfo;
import com.sundolls.epbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StudyInfoRepository extends JpaRepository<StudyInfo, Long> {
    List<StudyInfo> findByUserAndCreatedAtBetween(User user, LocalDateTime createdAtStart, LocalDateTime createdAtEnd);

}
