package com.sundolls.epbackend.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String id;
    private String password;
}
