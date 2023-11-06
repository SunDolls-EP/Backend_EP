package com.sundolls.epbackend.dto.response;

import com.sundolls.epbackend.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class QuestionResponse {
    @Schema(description = "질문 아이디")
    private Long id;

    @Schema(description = "작성자 이름", defaultValue = "엄준식")
    private String writerUsername;

    @Schema(description = "작성자 태그", defaultValue = "0001")
    private String writerTag;

    @Schema(description = "질문 제목", defaultValue = "질문 제목")
    private String title;

    @Schema(description = "내용", defaultValue = "내용")
    private String content;


    @Schema(description = "생성일", defaultValue = "2023-12-13T17:17:49")
    private LocalDateTime createdAt;
    @Schema(description = "마지막 수정일", defaultValue = "2023-12-13T17:17:49")
    private LocalDateTime modifiedAt;
}
