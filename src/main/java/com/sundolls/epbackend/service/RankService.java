package com.sundolls.epbackend.service;

import com.sundolls.epbackend.dto.response.SchoolRankResponse;
import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RankService {
    private final UserRepository userRepository;

    //학교 랭킹, 캐싱, 10개
//    @Cacheable(cacheNames = "schoolRank")
    public ResponseEntity<List<SchoolRankResponse>> getSchoolRanking(Integer limit) {
        List<SchoolRankResponse> response = userRepository.getSchoolRank(limit);
        return ResponseEntity.ok(response);
    }

    //학교내 랭킹, 10명
    public ResponseEntity<List<UserResponse>> getUserRanking(String schoolName, Integer limit) {
        List<UserResponse> response = userRepository.getPersonalRank(schoolName,limit);
        return ResponseEntity.ok(response);
    }

    //랭킹, 캐싱, 50명
//    @Cacheable(cacheNames ="personalRank")
    public ResponseEntity<List<UserResponse>> getUserRanking(Integer limit){
        List<UserResponse> response = userRepository.getPersonalRank(limit);
        return ResponseEntity.ok(response);
    }

//    @CacheEvict(cacheNames = {"schoolRank","personalRank"})
    public void deleteAllCache() {

    }
}
