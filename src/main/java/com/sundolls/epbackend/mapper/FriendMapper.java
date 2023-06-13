package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.FriendResponse;
import com.sundolls.epbackend.entity.Friend;
import com.sundolls.epbackend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

public interface FriendMapper {

    FriendResponse toDto(final Friend entity, final User requester);

    Friend toEntity(FriendResponse dto);

    default String getUsername(User user) {
        return user.getUsername();
    }
    default String getTag(User user) {
        return user.getTag();
    }

    default String getSchoolName(User user) {
        return user.getSchoolName();
    }

    default LocalDateTime getCreatedAt(User user) {
        return user.getCreatedAt();
    }
    default LocalDateTime getModifiedAt(User user) {
        return user.getModifiedAt();
    }

    default String getTargetUsername(User user, User target, User requester) {
        if (user.equals(requester)){
            return target.getUsername();
        }
        return user.getUsername();
    }

    default String getTargetTag(User user, User target, User requester) {
        if (user.equals(requester)){
            return target.getTag();
        }
        return user.getTag();
    }

    default String getTargetSchoolName(User user, User target, User requester) {
        if (user.equals(requester)){
            return target.getSchoolName();
        }
        return user.getSchoolName();
    }
}
