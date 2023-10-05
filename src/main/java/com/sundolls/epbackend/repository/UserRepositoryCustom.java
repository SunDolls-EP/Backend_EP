package com.sundolls.epbackend.repository;

import com.sundolls.epbackend.dto.response.RankResponse;

import java.util.List;

public interface UserRepositoryCustom {
    public List<RankResponse> getSchoolRank(Integer limit);
    public List<RankResponse> getPersonalRank(String schoolName, Integer limit);
    public List<RankResponse> getPersonalRank(Integer limit);
}
