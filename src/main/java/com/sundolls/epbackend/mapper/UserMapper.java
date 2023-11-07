package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.entity.User;

public class UserMapper{

    public static UserResponse toDto(User entity) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(entity.getUsername());
        userResponse.setTag(entity.getTag());
        userResponse.setSchoolName(entity.getSchoolName());
        userResponse.setTotalStudyTime(entity.getTotalStudyTime());
        userResponse.setProfileUrl(entity.getProfileUrl());
        userResponse.setCreatedAt(entity.getCreatedAt());
        userResponse.setModifiedAt(entity.getModifiedAt());
        return userResponse;
    }
}
