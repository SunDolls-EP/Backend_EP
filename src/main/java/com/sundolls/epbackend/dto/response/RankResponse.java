package com.sundolls.epbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
public class RankResponse {
    private String name;
    private Integer totalStudyTime;
}
