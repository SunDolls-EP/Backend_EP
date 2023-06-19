package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.request.QuestionRequest;
import com.sundolls.epbackend.dto.response.QuestionResponse;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.repository.QuestionRepository;
import com.sundolls.epbackend.repository.UserRepository;
import com.sundolls.epbackend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/QnA/question")
public class QuestionController {
    private final QuestionService questionService;
    private final JwtProvider jwtProvider;

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable Long questionId) {
        return questionService.getQuestion(questionId);

    }

    @GetMapping("/list")
    public ResponseEntity<Page<QuestionResponse>> getQuestionList(
            Pageable pageable,
            @RequestParam(name = "title-keyword", required = false) String title,
            @RequestParam(name = "content-keyword", required = false) String content,
            @RequestParam(name = "writer-name", required = false) String writerUsername,
            @RequestParam(name = "writer-tag", required = false) String writerTag,
            @RequestParam(name = "from", defaultValue = "2000-01-01 00")String from,
            @RequestParam(name = "to", defaultValue = "3000-12-31 23")String to
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        return questionService.getQuestions(pageable, writerUsername, writerTag, title, content, LocalDateTime.parse(from, formatter), LocalDateTime.parse(to, formatter));

    }

    @PostMapping("")
    public ResponseEntity<QuestionResponse> writeQuestion(
            @RequestHeader(name = "Authorization") String accessToken,
            @RequestBody QuestionRequest request
            ) {
        return questionService.writeQuestion(jwtProvider.getPayload(accessToken), request);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @RequestHeader(name = "Authorization") String accessToken,
            @PathVariable Long questionId,
            @RequestBody QuestionRequest request
    ){
        return questionService.updateQuestion(questionId, jwtProvider.getPayload(accessToken), request);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<QuestionResponse> deleteQuestion(
            @RequestHeader(name = "Authorization") String accessToken,
            @PathVariable Long questionId
    ){
        return questionService.deleteQuestion(questionId, jwtProvider.getPayload(accessToken));
    }

}
