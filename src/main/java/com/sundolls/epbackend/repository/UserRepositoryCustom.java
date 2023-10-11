package com.sundolls.epbackend.repository;

import com.sundolls.epbackend.dto.response.SchoolRankResponse;
import com.sundolls.epbackend.dto.response.UserResponse;

import java.util.List;

public interface UserRepositoryCustom {
    public List<SchoolRankResponse> getSchoolRank(Integer limit);
    public List<UserResponse> getPersonalRank(String schoolName, Integer limit);
    public List<UserResponse> getPersonalRank(Integer limit);

}
