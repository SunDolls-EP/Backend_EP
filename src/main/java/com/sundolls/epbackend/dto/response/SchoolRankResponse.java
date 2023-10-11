package com.sundolls.epbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SchoolRankResponse {
    private String name;
    private Integer totalStudyTime;
}
