package com.sundolls.epbackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FriendResponse {
    private String username;
    private String schoolName;
    private boolean accepted;
}
