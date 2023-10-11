package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.FriendResponse;
import com.sundolls.epbackend.entity.Friend;
import com.sundolls.epbackend.entity.User;

public interface FriendMapper {
    FriendResponse toDto(final Friend entity, final User requester);
}
