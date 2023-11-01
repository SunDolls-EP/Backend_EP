package com.sundolls.epbackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPatchRequest {
    @Schema(description = "바꿀 이름", defaultValue = "엄준식")
    private String username;

    @Schema(description = "바꿀 학교 이름", defaultValue = "대구 소프트웨어 마이스터 고등학교")
    private String schoolName;
}
