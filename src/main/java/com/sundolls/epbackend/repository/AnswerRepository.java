package com.sundolls.epbackend.repository;

import com.sundolls.epbackend.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
