package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.request.QuestionRequest;
import com.sundolls.epbackend.dto.response.QuestionResponse;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/QnA/question")
public class QuestionController {
    private final QuestionService questionService;
    private final JwtProvider jwtProvider;

    @GetMapping("/{questionId}")
    @Operation(summary = "질문 받아오기")
//    @Parameter(name = "questionId", description = "받아올 질문의 Id", dataTypeClass = Long.class)
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable Long questionId) throws Exception {
        return questionService.getQuestion(questionId);

    }

    @GetMapping("/list")
    @Operation(summary = "질문 리스트로 받아오기 (내용은 null)")
    @Parameters({
            @Parameter(name = "title", description = "질문의 제목에 포함될 키워드 (필수X)", required = false),
            @Parameter(name = "content", description = "질문의 내용에 포함될 키워드 (필수X)",  required = false),
            @Parameter(name = "writerUsername", description = "질문의 작성자 닉네임 (필수X)",  required = false),
            @Parameter(name = "writerTag", description = "질문의 작성자 태그 (필수X)", required = false),
            @Parameter(name = "from", description = "yyyy-mm-dd HH 형식으로 조회할 시작일 (기본값 2000-01-01 00)", required = true),
            @Parameter(name = "to", description = "yyyy-mm-dd HH 형식으로 조회할 종료일 (기본값 3000-12-31 23)", required = true)
    })
    public ResponseEntity<Page<QuestionResponse>> getQuestionList(
            @PageableDefault @Parameter(hidden = true) Pageable pageable,
            @RequestParam(name = "title-keyword", required = false) String title,
            @RequestParam(name = "content-keyword", required = false) String content,
            @RequestParam(name = "writer-name", required = false) String writerUsername,
            @RequestParam(name = "writer-tag", required = false) String writerTag,
            @RequestParam(name = "from", defaultValue = "2000-01-01 00")String from,
            @RequestParam(name = "to", defaultValue = "3000-12-31 23")String to
    ) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        return questionService.getQuestions(pageable, writerUsername, writerTag, title, content, LocalDateTime.parse(from, formatter), LocalDateTime.parse(to, formatter));

    }

    @PostMapping("")
    @Operation(summary = "질문 작성")
    public ResponseEntity<QuestionResponse> writeQuestion(
            @AuthenticationPrincipal @Parameter(hidden = true) User user,
            @RequestBody QuestionRequest request
            ) throws Exception {
        return questionService.writeQuestion(user, request);
    }

    @PutMapping("/{questionId}")
    @Operation(summary = "질문 수정")
    @Parameter(name = "questionId", description = "수정할 질문의 Id", required = true)
    public ResponseEntity<QuestionResponse> updateQuestion(
            @AuthenticationPrincipal @Parameter(hidden = true) User user,
            @PathVariable Long questionId,
            @RequestBody QuestionRequest request
    ) throws Exception {
        return questionService.updateQuestion(questionId, user, request);
    }

    @DeleteMapping("/{questionId}")
    @Operation(summary = "질문 삭제")
    @Parameter(name = "questionId", description = "삭제할 질문의 Id", required = true)
    public ResponseEntity<QuestionResponse> deleteQuestion(
            @AuthenticationPrincipal @Parameter(hidden = true) User user,
            @PathVariable Long questionId
    ) throws Exception {
        return questionService.deleteQuestion(questionId, user);
    }

}
