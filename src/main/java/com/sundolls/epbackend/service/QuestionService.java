package com.sundolls.epbackend.service;

import com.querydsl.core.Tuple;
import com.sundolls.epbackend.dto.request.QuestionRequest;
import com.sundolls.epbackend.dto.response.QuestionResponse;
import com.sundolls.epbackend.entity.Question;
import com.sundolls.epbackend.entity.User;
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

    public ResponseEntity<QuestionResponse> writeQuestion(Jws<Claims> payload, QuestionRequest request) {
        HttpStatus status = HttpStatus.OK;

        User writer = getUser(payload);

        Question question = Question.builder()
                .user(writer)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        questionRepository.save(question);

        QuestionResponse body = questionMapper.toDto(question);

        return new ResponseEntity<>(body, status);
    }

    public ResponseEntity<Page<QuestionResponse>> getQuestions(Pageable pageable, String username, String tag, String title, String content, LocalDateTime from, LocalDateTime to) {
        HttpStatus status = HttpStatus.OK;

        Page<Question> questions = questionRepository.searchQuestions(pageable, username, tag, title, content, from, to);
        Page<QuestionResponse> body = questions.map(questionMapper::toDto);

        return new ResponseEntity<>(body, status);
    }

    public ResponseEntity<QuestionResponse> getQuestion(Long questionId) {
        HttpStatus status = HttpStatus.OK;

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        }
        Question question = optionalQuestion.get();

        QuestionResponse body = questionMapper.toDto(question);

        return new ResponseEntity<>(body, status);
    }

    public ResponseEntity<QuestionResponse> deleteQuestion(Long questionId, Jws<Claims> payload) {
        HttpStatus status = HttpStatus.OK;

        User user = getUser(payload);

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        }
        Question question = optionalQuestion.get();
        if (!user.equals(question.getUser())) {
            status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<>(status);
        }

        questionRepository.deleteById(questionId);

        return new ResponseEntity<>(questionMapper.toDto(question), status);
    }
    @Transactional
    public ResponseEntity<QuestionResponse> updateQuestion(Long questionId, Jws<Claims> payload, QuestionRequest request) {
        HttpStatus status = HttpStatus.OK;

        User user = getUser(payload);

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        }
        Question question = optionalQuestion.get();
        if (!user.equals(question.getUser())) {
            status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<>(status);
        }

        question.update(request.getTitle(), request.getContent());

        return new ResponseEntity<>(questionMapper.toDto(question), status);
    }

    private User getUser(Jws<Claims> payload){
        Optional<User> optionalUser = userRepository.findByUsernameAndTag((String) payload.getBody().get("username"), (String) payload.getBody().get("tag"));
        if (optionalUser.isEmpty()) {
            return null;
        }
        return optionalUser.get();
    }


}
