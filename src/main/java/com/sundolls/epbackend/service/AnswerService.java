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
import lombok.RequiredArgsConstructor;
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

    public ResponseEntity<AnswerResponse> postAnswer(Long questionId , String userId, AnswerRequest request) {
        HttpStatus status = HttpStatus.OK;

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(status);
        }
        User user = optionalUser.get();

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        }
        Question question = optionalQuestion.get();

        Answer answer = Answer.builder()
                .user(user)
                .question(question)
                .content(request.getContent())
                .build();
        answerRepository.save(answer);

        return new ResponseEntity<>(AnswerMapper.MAPPER.toDto(answer), status);

    }

    public ResponseEntity<List<AnswerResponse>> getAnswers(Long questionId, Pageable pageable){
        HttpStatus status = HttpStatus.OK;

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        }
        Question question = optionalQuestion.get();

        List<Answer> answers = answerRepository.findByQuestion(question, pageable);

        List<AnswerResponse> body = answers.stream().map(AnswerMapper.MAPPER::toDto).toList();

        return new ResponseEntity<>(body, status);

    }

    public ResponseEntity<AnswerResponse> updateAnswer(Long answerId, String userId, AnswerRequest request) {
        HttpStatus status = HttpStatus.OK;

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(status);
        }
        User user = optionalUser.get();

        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isEmpty()) {
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        }
        Answer answer = optionalAnswer.get();

        optionalUser = userRepository.findById(answer.getUser().getId());
        if (optionalUser.isEmpty()) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(status);
        }
        User answerWriter = optionalUser.get();

        if (!user.equals(answerWriter)) {
            status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<>(status);
        }

        answer.update(request.getContent());
        answerRepository.save(answer);

        return new ResponseEntity<>(AnswerMapper.MAPPER.toDto(answer), status);

    }

}
