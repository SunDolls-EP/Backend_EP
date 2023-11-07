package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.FriendResponse;
import com.sundolls.epbackend.entity.Friend;
import com.sundolls.epbackend.entity.User;


public class FriendMapper {

    public static FriendResponse toDto(Friend entity, User requester) {
        if ( entity == null ) {
            return null;
        }

        FriendResponse friendResponse = new FriendResponse();
        if (requester.equals(entity.getUser())) {
            friendResponse.setUsername(entity.getTargetUser().getUsername());
            friendResponse.setTag(entity.getTargetUser().getTag());
            friendResponse.setSchoolName(entity.getTargetUser().getSchoolName());
            friendResponse.setTotalStudyTime(entity.getTargetUser().getTotalStudyTime());
        } else {
            friendResponse.setUsername(entity.getUser().getUsername());
            friendResponse.setTag(entity.getUser().getTag());
            friendResponse.setSchoolName(entity.getUser().getSchoolName());
            friendResponse.setTotalStudyTime(entity.getUser().getTotalStudyTime());
        }

        friendResponse.setAccepted(entity.isAccepted());
        friendResponse.setCreatedAt(entity.getCreatedAt());
        friendResponse.setModifiedAt(entity.getModifiedAt());
        friendResponse.setAccepted(entity.isAccepted());

        return friendResponse;
    }
}

