package com.sundolls.epbackend.service;

import com.querydsl.core.Tuple;
import com.sundolls.epbackend.dto.request.QuestionRequest;
import com.sundolls.epbackend.dto.response.QuestionResponse;
import com.sundolls.epbackend.entity.Question;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.execption.CustomException;
import com.sundolls.epbackend.execption.ErrorCode;
import com.sundolls.epbackend.mapper.QuestionMapper;
import com.sundolls.epbackend.repository.QuestionRepository;
import com.sundolls.epbackend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuestionMapper questionMapper;

    public ResponseEntity<QuestionResponse> writeQuestion(User user, QuestionRequest request) {

        Question question = Question.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        questionRepository.save(question);

        QuestionResponse body = questionMapper.toDto(question);

        return ResponseEntity.ok(body);
    }

    public ResponseEntity<Page<QuestionResponse>> getQuestions(Pageable pageable, String username, String tag, String title, String content, LocalDateTime from, LocalDateTime to) {
        HttpStatus status = HttpStatus.OK;

        Page<Question> questions = questionRepository.searchQuestions(pageable, username, tag, title, content, from, to);
        Page<QuestionResponse> body = questions.map(questionMapper::toDto);

        return new ResponseEntity<>(body, status);
    }

    public ResponseEntity<QuestionResponse> getQuestion(Long questionId) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        QuestionResponse body = questionMapper.toDto(question);

        return ResponseEntity.ok(body);
    }

    public ResponseEntity<QuestionResponse> deleteQuestion(Long questionId, User user) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        if (!user.equals(question.getUser())) {
            throw new CustomException(ErrorCode.QUESTION_FORBIDDEN);
        }

        questionRepository.deleteById(questionId);

        return ResponseEntity.ok(questionMapper.toDto(question));
    }
    @Transactional
    public ResponseEntity<QuestionResponse> updateQuestion(Long questionId, User user, QuestionRequest request) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        if (!user.equals(question.getUser())) {
            throw new CustomException(ErrorCode.QUESTION_FORBIDDEN);
        }

        question.update(request.getTitle(), request.getContent());

        return ResponseEntity.ok(questionMapper.toDto(question));
    }
}
