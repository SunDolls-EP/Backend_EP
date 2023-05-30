package com.sundolls.epbackend.repository;

import com.sundolls.epbackend.entity.Answer;
import com.sundolls.epbackend.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion(Question question, Pageable pageable);

}
