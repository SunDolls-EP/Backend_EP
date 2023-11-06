package com.sundolls.epbackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SchoolRankResponse {
    @Schema(description = "학교 이름", defaultValue = "대구 소프트웨어 마이스터 고등학교")
    private String name;

    @Schema(description = "총 공부 시간", defaultValue = "12345")
    private Integer totalStudyTime;
}
