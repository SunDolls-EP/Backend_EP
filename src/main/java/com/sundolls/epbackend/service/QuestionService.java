package com.sundolls.epbackend.service;

import com.sundolls.epbackend.dto.request.QuestionRequest;
import com.sundolls.epbackend.dto.response.QuestionResponse;
import com.sundolls.epbackend.entity.Question;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.mapper.QuestionMapper;
import com.sundolls.epbackend.repository.QuestionRepository;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public QuestionResponse writeQuestion(String userId, QuestionRequest request) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return null;
        User writer = optionalUser.get();

        Question question = Question.builder()
                .user(writer)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        questionRepository.save(question);

        return QuestionMapper.MAPPER.toDto(question);
    }

    public Page<QuestionResponse> getQuestions(Pageable pageable, String username, String title, String content, LocalDateTime from, LocalDateTime to) {
        Page<Question> questions = questionRepository.searchQuestions(pageable, username, title, content, from, to);
        return questions.map(QuestionMapper.MAPPER::toDto);
    }

    public ResponseEntity<QuestionResponse> updateQuestion(Long questionId, String userId, QuestionRequest request) {
        HttpStatus status = HttpStatus.OK;

        User user = userRepository.findById(userId).get();

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
        questionRepository.save(question);

        return new ResponseEntity<>(QuestionMapper.MAPPER.toDto(question), status);
    }


}
