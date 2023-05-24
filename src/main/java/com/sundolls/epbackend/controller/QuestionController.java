package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.response.QuestionResponse;
import com.sundolls.epbackend.repository.QuestionRepository;
import com.sundolls.epbackend.repository.UserRepository;
import com.sundolls.epbackend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/QnA/question")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("")
    public ResponseEntity<Page<QuestionResponse>> getQuestions(
            Pageable pageable,
            @RequestParam(name = "title-keyword", required = false) String title,
            @RequestParam(name = "content-keyword", required = false) String content,
            @RequestParam(name = "writer", required = false) String writerUsername,
            @RequestParam(name = "from", defaultValue = "2000-01-01 00")String from,
            @RequestParam(name = "to", defaultValue = "3000-12-31 23")String to
            ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        Page<QuestionResponse> body = questionService.getQuestions(pageable, writerUsername, title, content)


        return null;
    }
}
