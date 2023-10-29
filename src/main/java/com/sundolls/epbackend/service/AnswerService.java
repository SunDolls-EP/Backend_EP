package com.sundolls.epbackend.service;

import com.sundolls.epbackend.dto.request.AnswerRequest;
import com.sundolls.epbackend.dto.response.AnswerResponse;
import com.sundolls.epbackend.entity.Answer;
import com.sundolls.epbackend.entity.Question;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.mapper.AnswerMapper;
import com.sundolls.epbackend.repository.AnswerRepository;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerMapper answerMapper;

    public ResponseEntity<AnswerResponse> postAnswer(Long questionId , User user, AnswerRequest request) {

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        Question question = optionalQuestion.get();

        Answer answer = Answer.builder()
                .user(user)
                .question(question)
                .content(request.getContent())
                .build();
        answerRepository.save(answer);

        return ResponseEntity.ok(answerMapper.toDto(answer));

    }

    public ResponseEntity<Page<AnswerResponse>> getAnswers(Long questionId, Pageable pageable){
        HttpStatus status = HttpStatus.OK;

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        }
        Question question = optionalQuestion.get();

        Page<Answer> answers = answerRepository.findByQuestion(question, pageable);

        Page<AnswerResponse> body = answers.map(answerMapper::toDto);

        return new ResponseEntity<>(body, status);

    }

    public ResponseEntity<AnswerResponse> updateAnswer(Long answerId, User user, AnswerRequest request) {

        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Answer answer = optionalAnswer.get();

        User answerWriter = userRepository.findById(answer.getUser().getId()).get();

        if (!user.equals(answerWriter)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        answer.update(request.getContent());
        answerRepository.save(answer);

        return ResponseEntity.ok(answerMapper.toDto(answer));

    }

    public ResponseEntity<AnswerResponse> deleteAnswer(Long answerId, User user) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Answer answer = optionalAnswer.get();

        User answerWriter = userRepository.findById(answer.getUser().getId()).get();

        if (!user.equals(answerWriter)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        answerRepository.delete(answer);
        return ResponseEntity.ok(answerMapper.toDto(answer));
    }
}
