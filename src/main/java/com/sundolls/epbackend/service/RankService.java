package com.sundolls.epbackend.service;

import com.sundolls.epbackend.dto.response.RankResponse;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RankService {
    private final UserRepository userRepository;

    //학교 랭킹, 캐싱, 10개
    @Cacheable(cacheNames = "schoolRank")
    public ResponseEntity<List<RankResponse>> getSchoolRanking() {
        List<RankResponse> response = userRepository.getSchoolRank(10);
        return ResponseEntity.ok(response);
    }

    //학교내 랭킹, 10명
    public ResponseEntity<List<RankResponse>> getUserRanking(String schoolName) {
        List<RankResponse> response = userRepository.getPersonalRank(schoolName,10);
        return ResponseEntity.ok(response);
    }

    //랭킹, 캐싱, 50명
    @Cacheable(cacheNames ="personalRank")
    public ResponseEntity<List<RankResponse>> getUserRanking(){
        List<RankResponse> response = userRepository.getPersonalRank(50);
        return ResponseEntity.ok(response);
    }

    @CacheEvict(cacheNames = {"schoolRank","personalRank"})
    public void deleteAllCache() {

    }
}
