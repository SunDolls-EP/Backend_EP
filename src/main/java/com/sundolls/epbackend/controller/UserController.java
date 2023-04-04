package com.sundolls.epbackend.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.sundolls.epbackend.dto.UserDto;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final FirebaseAuth firebaseAuth;
    private final UserService userService;

    @PostMapping("/signup")
    public void signUp(@RequestHeader("Authorization") String authorization,
                       @RequestBody UserDto userDto){
        User user = userService.register(authorization,userDto);
    }
}
