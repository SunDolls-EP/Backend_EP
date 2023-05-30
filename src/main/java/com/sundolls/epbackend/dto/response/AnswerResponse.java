package com.sundolls.epbackend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerResponse {
    private String writerName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
