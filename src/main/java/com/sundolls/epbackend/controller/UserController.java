package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.domain.dto.UserDto;
import com.sundolls.epbackend.domain.entity.User;
import com.sundolls.epbackend.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp( @RequestBody UserDto userDto){
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        UserDto returnDto = userService.register( userDto);

        return new ResponseEntity<>(returnDto, header,HttpStatus.OK);
    }
}
