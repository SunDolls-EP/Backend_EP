package com.sundolls.epbackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPatchRequest {
    private String username;
    private String  schoolName;
}
