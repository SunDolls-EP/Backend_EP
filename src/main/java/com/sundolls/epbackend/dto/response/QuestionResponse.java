package com.sundolls.epbackend.dto.response;

import com.sundolls.epbackend.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class QuestionResponse {
    private Long id;
    private String writerUsername;
    private String writerTag;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
