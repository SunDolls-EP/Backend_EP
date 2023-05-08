package com.sundolls.epbackend.dto.response;


import com.sundolls.epbackend.dto.request.JoinRequest;
import com.sundolls.epbackend.entity.User;
import lombok.*;

@NoArgsConstructor
@Getter
public class UserResponse{
    private String username;
    private String password;
    private String email;
    private String schoolName;
    private String message;

    @Builder
    public UserResponse(String username, String password, String email, String schoolName, String message){
        this.username = username;
        this.password = password;
        this.email = email;
        this.schoolName = schoolName;
        this.message = message;
    }
}
