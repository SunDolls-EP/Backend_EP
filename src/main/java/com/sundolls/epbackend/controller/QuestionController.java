package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.domain.dto.QuestionDto;
import com.sundolls.epbackend.domain.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/create")
    public void createQuestion(@RequestBody QuestionDto questionDto){
        questionService.createQuestion(questionDto);
    }
}
