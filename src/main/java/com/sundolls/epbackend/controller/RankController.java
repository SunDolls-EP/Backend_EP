package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.response.SchoolRankResponse;
import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.service.RankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/rank")
@RequiredArgsConstructor
@Slf4j
public class RankController {
    private final RankService rankService;

    @GetMapping("/school/all")
    @Operation(summary = "학교 랭킹")
    public ResponseEntity<List<SchoolRankResponse>> getSchoolRank() {
       return rankService.getSchoolRanking(15);
    }

    @GetMapping("/school/{schoolName}")
    @Operation(summary = "학교내 랭킹")
    @Parameter(name = "schoolName", description = "학교 이름", required = true)
    public ResponseEntity<List<UserResponse>> getInSchoolRank(@PathVariable(name = "schoolName")String schoolName) {
        return rankService.getUserRanking(schoolName, 15);
    }

    @GetMapping("/")
    @Operation(summary = "개인 랭킹")
    public ResponseEntity<List<UserResponse>> getPersonalRank() {
        return rankService.getUserRanking(15);
    }
}
