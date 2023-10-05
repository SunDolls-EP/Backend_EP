package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.response.RankResponse;
import com.sundolls.epbackend.service.RankService;
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
    public ResponseEntity<List<RankResponse>> getSchoolRank() {
       return rankService.getSchoolRanking();
    }

    @GetMapping("/school/{schoolName}")
    public ResponseEntity<List<RankResponse>> getInSchoolRank(@PathVariable(name = "schoolName")String schoolName) {
        return rankService.getUserRanking(schoolName);
    }

    @GetMapping("/")
    public ResponseEntity<List<RankResponse>> getPersonalRank() {
        return rankService.getUserRanking();
    }
}
