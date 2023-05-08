package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @RequestHeader(value = "Authorization")String idTokenString) throws GeneralSecurityException, IOException {
        User user = userService.join(idTokenString);

        HttpStatus httpStatus = null;
        UserResponse body = null;
        httpStatus = HttpStatus.OK;

        return new ResponseEntity<>(body,httpStatus);
    }

}
