package com.sundolls.epbackend.dto.request;

import lombok.Data;

@Data
public class JoinRequest {
    private String id;
    private String username;
    private String password;
    private String email;
    private String schoolName;
}
