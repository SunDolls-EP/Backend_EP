package com.sundolls.epbackend.service;

import com.sundolls.epbackend.dto.request.QuestionRequest;
import com.sundolls.epbackend.dto.response.QuestionResponse;
import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.entity.Question;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.mapper.impl.QuestionMapper;
import com.sundolls.epbackend.repository.QuestionRepository;
import com.sundolls.epbackend.repository.UserRepository;
import com.sundolls.epbackend.repository.impl.QuestionRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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



}
