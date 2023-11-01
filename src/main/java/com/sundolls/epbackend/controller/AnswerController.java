package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.request.AnswerRequest;
import com.sundolls.epbackend.dto.response.AnswerResponse;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/QnA/answer")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;
    private final JwtProvider jwtProvider;

    @PostMapping("/{questionId}")
    @Operation(summary = "답변 달기")
    @Parameter(name = "questionId", description = "답변을 달 질문의 Id", required = true)
    public ResponseEntity<AnswerResponse> postAnswer(
            @AuthenticationPrincipal @Parameter(hidden = true) User user,
            @RequestBody AnswerRequest request,
            @PathVariable(name = "questionId") Long questionId
            ){
        return answerService.postAnswer(questionId, user, request);
    }

    @GetMapping("/{questionId}")
    @Operation(summary = "답변 가져오기")
    @Parameter(name = "questionId", description = "답변을 가져올 질문의 Id", required = true)
    public ResponseEntity<Page<AnswerResponse>> getAnswers(
            @PathVariable(name = "questionId") Long questionId,
            @PageableDefault @Parameter(hidden = true) Pageable pageable
    ) {
        return answerService.getAnswers(questionId, pageable);
    }

    @PutMapping("/{answerId}")
    @Operation(summary = "답변 수정")
    @Parameter(name = "answerId", description = "수정할 답변의 Id", required = true)
    public ResponseEntity<AnswerResponse> updateAnswer(
            @AuthenticationPrincipal @Parameter(hidden = true) User user,
            @RequestBody AnswerRequest request,
            @PathVariable(name = "answerId") Long answerId
    ) {
        return answerService.updateAnswer(answerId, user, request);
    }

    @DeleteMapping("/{answerId}")
    @Operation(summary = "답변 삭제")
    @Parameter(name = "answerId", description = "삭제할 답변의 Id", required = true)
    public ResponseEntity<AnswerResponse> deleteAnswer(
            @AuthenticationPrincipal @Parameter(hidden = true) User user,
            @PathVariable(name = "answerId") Long answerId
    ) {
        return answerService.deleteAnswer(answerId, user);
    }
}
