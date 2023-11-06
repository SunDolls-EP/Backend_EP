package com.sundolls.epbackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerResponse {
    @Schema(description = "작성자 이름", defaultValue = "엄준식")
    private String writerName;

    @Schema(description = "작성자 태그", defaultValue = "0001")
    private String tag;

    @Schema(description = "내용", defaultValue = "내용")
    private String content;

    @Schema(description = "생성일", defaultValue = "2023-12-13T17:17:49")
    private LocalDateTime createdAt;
    @Schema(description = "마지막 수정일", defaultValue = "2023-12-13T17:17:49")
    private LocalDateTime modifiedAt;
}
