package com.sundolls.epbackend.service;


import com.sundolls.epbackend.dto.QuestionDto;
import com.sundolls.epbackend.entity.Question;
import com.sundolls.epbackend.repository.QuestionRepository;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public void createQuestion(String header,QuestionDto questionDto){


    }

    public void deleteQuestion(long id){
        questionRepository.deleteById(id);
    }

    public void updateQuestion(long id,QuestionDto questionDto){
        Optional<Question> question = questionRepository.findById(id);
        if(question.isPresent()){
            questionRepository.deleteById(id);
            Question existingQuestion = question.get();
            Question newQuestion = Question.builder()
                    .title(questionDto.getTitle())
                    .content(questionDto.getContent())
                    .build();
            questionRepository.save(newQuestion);
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
