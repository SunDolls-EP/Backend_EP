package com.sundolls.epbackend.domain.service;


import com.sundolls.epbackend.domain.dto.QuestionDto;
import com.sundolls.epbackend.domain.entity.Question;
import com.sundolls.epbackend.domain.entity.User;
import com.sundolls.epbackend.repository.QuestionRepository;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public void createQuestion(QuestionDto questionDto){
        User user = userRepository.findById(questionDto.getUserId()).get();
        Question question = Question.builder()
                .title(questionDto.getTitle())
                .content(questionDto.getContent())
                .user(user)
                .build();
        questionRepository.save(question);

    }
}
