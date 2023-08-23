package com.sundolls.epbackend.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UserResponse {
    private String username;
    private String tag;
    private String schoolName;
    private Integer totalStudyTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
