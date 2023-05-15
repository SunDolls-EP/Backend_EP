package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.request.UserPatchRequest;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @GetMapping("/token")
    public ResponseEntity<Map> login(
            @RequestHeader(value = "Authorization")String idTokenString ) throws GeneralSecurityException, IOException {

        User user = userService.join(idTokenString);
        HttpStatus httpStatus = null;
        Map<String, Object> body = new HashMap<>();;
        if(user!=null) {
            httpStatus=HttpStatus.OK;
            body.put("user",user);
            body.put("access-token",jwtProvider.generateToken(user.getId()));

        }
        else{
            httpStatus=HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(body,httpStatus);
    }

    @PatchMapping("/user")
    public ResponseEntity<Map> changeUsername(
            @RequestHeader(value = "Authorization")String accessTokenString, UserPatchRequest request){
        User user = userService.updateUser(request,accessTokenString);
        HttpStatus httpStatus = null;
        Map<String, Object> body = new HashMap<>();
        if (user!=null){
            httpStatus = HttpStatus.OK;
            body.put("user",user);
            body.put("modified_at",user.getModifiedAt());
        }
        else {
            httpStatus=HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(body,httpStatus);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Map> findUser(@PathVariable String username){
        User user = userService.findUser(username);
        HttpStatus httpStatus = null;
        Map<String, Object> body = new HashMap<>();
        if(user!=null){
            httpStatus = HttpStatus.OK;
            body.put("username",user.getUsername());
            body.put("school",user.getSchoolName());
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(body, httpStatus);
   }

}
