package com.sundolls.epbackend.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FriendResponse extends UserResponse{
    private boolean accepted;
}
