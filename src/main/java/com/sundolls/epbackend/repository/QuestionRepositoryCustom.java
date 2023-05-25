package com.sundolls.epbackend.repository;

import com.sundolls.epbackend.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface QuestionRepositoryCustom{
    Page<Question> searchQuestions(Pageable pageable, String username, String title, String content, LocalDateTime from, LocalDateTime to);
}
