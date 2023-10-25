package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.response.SchoolRankResponse;
import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.service.RankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/rank")
public class RankController {
    private final RankService rankService;

    @GetMapping("/school/all")
    @Operation(summary = "학교 랭킹")
    public ResponseEntity<List<SchoolRankResponse>> getSchoolRank(@RequestParam(name = "limit", defaultValue = "15") Integer limit) {
       return rankService.getSchoolRanking(limit);
    }

    @GetMapping("/school/{schoolName}")
    @Operation(summary = "학교내 랭킹")
    @Parameter(name = "schoolName", description = "학교 이름", required = true)
    public ResponseEntity<List<UserResponse>> getInSchoolRank(
    @PathVariable(name = "schoolName")String schoolName,
    @RequestParam(name = "limit", defaultValue = "15") Integer limit) {
        return rankService.getUserRanking(schoolName, limit);
    }

    @GetMapping("")
    @Operation(summary = "개인 랭킹")
    public ResponseEntity<List<UserResponse>> getPersonalRank(@RequestParam(name = "limit", defaultValue = "15") Integer limit) {
        return rankService.getUserRanking(limit);
    }
}
