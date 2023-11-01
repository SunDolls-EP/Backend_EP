package com.sundolls.epbackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponse {
    @Schema(defaultValue = "엄준식", description = "사용자 이름")
    private String username;

    @Schema(defaultValue = "0001", description = "사용자 태그")
    private String tag;

    @Schema(description = "학교이름", defaultValue = "대구 소프트에어 마이스터 고등학교")
    private String schoolName;

    @Schema(description = "총 공부 시간", defaultValue = "총 공부 시간")
    private Integer totalStudyTime;

    @Schema(description = "프로필 사진 URL", defaultValue = "프로필 사진 URL")
    private String profileUrl;

    @Schema(description = "생성일", defaultValue = "2023-12-13T17:17:49")
    private LocalDateTime createdAt;

    @Schema(description = "마지막 수정일", defaultValue = "2023-12-13T17:17:49")
    private LocalDateTime modifiedAt;
}
