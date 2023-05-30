package com.sundolls.epbackend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
public class RankResponse {
    private String username;
    private Time studyTime;
    private Integer rank;
}
