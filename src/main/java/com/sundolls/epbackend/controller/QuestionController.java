package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.domain.dto.QuestionDto;
import com.sundolls.epbackend.domain.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/create")
    public void createQuestion(@RequestHeader("Authorization") String authorization
            ,@RequestBody QuestionDto questionDto){
        questionService.createQuestion(authorization,questionDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteQuestion(@PathVariable long id){
        questionService.deleteQuestion(id);
    }

    @PutMapping("/update/{id}")
    public void updateQuestion(@PathVariable long id
            , @RequestBody QuestionDto questionDto){
        questionService.updateQuestion(id,questionDto);
    }

}
