package com.sundolls.epbackend.controller;


import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;

    @GetMapping("/login/oauth2/{provider}")
    @Operation(summary = "Oauth2 로그인")
    @Parameter(name = "provider", description = "Oauth2 프로바이더 (google, kakao)", required = true)
    public ResponseEntity<UserResponse> oauthLogin(
            @PathVariable(name = "provider") String provider,
            @RequestHeader(name = "Authorization")String idTokenString) {

        return userService.oauthLogin(provider, idTokenString);
    }

    @GetMapping("/login/refresh")
    @Operation(summary = "refresh token으로 access token 재발급")
    public ResponseEntity<UserResponse> refreshLogin(
            @RequestHeader(name = "Authorization")String refreshTokenString) {

        return userService.refreshLogin(refreshTokenString);
    }
}
