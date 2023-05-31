package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.request.AnswerRequest;
import com.sundolls.epbackend.dto.response.AnswerResponse;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/QnA/answer")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;
    private final JwtProvider jwtProvider;

    @PostMapping("/{questionId}")
    public ResponseEntity<AnswerResponse> postAnswer(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody AnswerRequest request,
            @PathVariable(name = "questionId") Long questionId
            ){
        return answerService.postAnswer(questionId, jwtProvider.getPayload(accessToken), request);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<List<AnswerResponse>> getAnswers(
            @PathVariable(name = "questionId") Long questionId,
            Pageable pageable
    ) {
        return answerService.getAnswers(questionId, pageable);
    }

    @PutMapping("/{answerId}")
    public ResponseEntity<AnswerResponse> updateAnswer(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody AnswerRequest request,
            @PathVariable(name = "answerId") Long answerId
    ) {
        return answerService.updateAnswer(answerId, jwtProvider.getPayload(accessToken), request);
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<AnswerResponse> deleteAnswer(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable(name = "answerId") Long answerId
    ) {
        return answerService.deleteAnswer(answerId, jwtProvider.getPayload(accessToken));
    }
}
