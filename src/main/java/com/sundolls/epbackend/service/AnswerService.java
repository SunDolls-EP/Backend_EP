package com.sundolls.epbackend.service;

import com.sundolls.epbackend.dto.request.AnswerRequest;
import com.sundolls.epbackend.dto.response.AnswerResponse;
import com.sundolls.epbackend.entity.Answer;
import com.sundolls.epbackend.entity.Question;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.execption.CustomException;
import com.sundolls.epbackend.execption.ErrorCode;
import com.sundolls.epbackend.mapper.AnswerMapper;
import com.sundolls.epbackend.repository.AnswerRepository;
import com.sundolls.epbackend.repository.QuestionRepository;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public ResponseEntity<AnswerResponse> postAnswer(Long questionId , User user, AnswerRequest request) {

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            throw new CustomException(ErrorCode.QUESTION_NOT_FOUND);
        }
        Question question = optionalQuestion.get();

        Answer answer = Answer.builder()
                .user(user)
                .question(question)
                .content(request.getContent())
                .build();
        answerRepository.save(answer);

        return ResponseEntity.ok(AnswerMapper.toDto(answer));

    }

    public ResponseEntity<Page<AnswerResponse>> getAnswers(Long questionId, Pageable pageable){

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            throw new CustomException(ErrorCode.QUESTION_NOT_FOUND);
        }
        Question question = optionalQuestion.get();

        Page<Answer> answers = answerRepository.findByQuestion(question, pageable);

        Page<AnswerResponse> body = answers.map(AnswerMapper::toDto);

        return ResponseEntity.ok(body);

    }

    public ResponseEntity<AnswerResponse> updateAnswer(Long answerId, User user, AnswerRequest request) {

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

        User answerWriter = userRepository.findById(answer.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.equals(answerWriter)) {
            throw new CustomException(ErrorCode.ANSWER_FORBIDDEN);
        }

        answer.update(request.getContent());
        answerRepository.save(answer);

        return ResponseEntity.ok(AnswerMapper.toDto(answer));

    }

    public ResponseEntity<AnswerResponse> deleteAnswer(Long answerId, User user) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

        User answerWriter = userRepository.findById(answer.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.equals(answerWriter)) {
            throw new CustomException(ErrorCode.ANSWER_FORBIDDEN);
        }

        answerRepository.delete(answer);
        return ResponseEntity.ok(AnswerMapper.toDto(answer));
    }
}
