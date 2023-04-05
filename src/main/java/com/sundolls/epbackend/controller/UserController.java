package com.sundolls.epbackend.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.sundolls.epbackend.domain.dto.UserDto;
import com.sundolls.epbackend.domain.entity.User;
import com.sundolls.epbackend.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final FirebaseAuth firebaseAuth;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestHeader("Authorization") String authorization,
                                 @RequestBody UserDto userDto){
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        User user = userService.register(authorization,userDto);
        return new ResponseEntity<>(userDto, header,HttpStatus.valueOf("200"));
    }
}
