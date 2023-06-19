package com.sundolls.epbackend.mapper.impl;

import com.sundolls.epbackend.dto.response.FriendResponse;
import com.sundolls.epbackend.entity.Friend;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.mapper.FriendMapper;
import org.springframework.stereotype.Component;

@Component
public class FriendMapperImpl implements FriendMapper {

    @Override
    public Friend toEntity(FriendResponse dto) {
        if ( dto == null ) {
            return null;
        }

        Friend.FriendBuilder friend = Friend.builder();

        friend.accepted( dto.isAccepted() );

        return friend.build();
    }

    @Override
    public FriendResponse toDto(Friend entity, User requester) {
        if ( entity == null ) {
            return null;
        }

        FriendResponse friendResponse = new FriendResponse();

        friendResponse.setUsername( getTargetUsername(entity.getUser(), entity.getTargetUser(), requester));
        friendResponse.setTag( getTargetTag(entity.getUser(), entity.getTargetUser(), requester) );
        friendResponse.setSchoolName( getTargetSchoolName(entity.getUser(), entity.getTargetUser(), requester) );
        friendResponse.setCreatedAt( getCreatedAt( entity.getUser() ) );
        friendResponse.setModifiedAt( getModifiedAt( entity.getUser() ) );
        friendResponse.setAccepted( entity.isAccepted() );

        return friendResponse;
    }
}

