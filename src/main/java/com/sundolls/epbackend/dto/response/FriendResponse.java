package com.sundolls.epbackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString

public class FriendResponse extends UserResponse{

    @Schema(description = "수락 여부", defaultValue = "true")
    private boolean accepted;

    public FriendResponse(){
        super();

    }

    public FriendResponse(String username, String tag, String schoolName, Integer totalStudyTime, String profileUrl, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        super(username, tag, schoolName, totalStudyTime, profileUrl, createdAt, modifiedAt);
    }
}
