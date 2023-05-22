package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.request.UserPatchRequest;
import com.sundolls.epbackend.dto.response.FriendResponse;
import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.repository.UserRepository;
import com.sundolls.epbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @GetMapping("/token")
    public ResponseEntity<UserResponse> login(
            @RequestHeader(value = "Authorization")String idTokenString ) throws GeneralSecurityException, IOException {

        User user = userService.join(idTokenString);
        HttpStatus httpStatus = null;
        UserResponse body = new UserResponse();
        HttpHeaders headers = new HttpHeaders();
        if(user!=null) {
            httpStatus=HttpStatus.OK;
            setUserResponseBody(body, user);
            headers.add("Authorization", jwtProvider.generateToken(user.getId()));

        }
        else{
            httpStatus=HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(body, headers, httpStatus);
    }

    @PatchMapping("/user")
    public ResponseEntity<UserResponse> updateUserInfo(
            @RequestHeader(value = "Authorization")String accessTokenString,
            @RequestBody UserPatchRequest request){
        User user = userService.updateUser(request, jwtProvider.getUsername(accessTokenString));
        HttpStatus httpStatus = null;
        UserResponse body = new UserResponse();
        if (user!=null){
            httpStatus = HttpStatus.OK;
            setUserResponseBody(body, user);
        }
        else {
            httpStatus=HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(body,httpStatus);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<UserResponse> findUser(@PathVariable String username){
        User user = userService.findUser(username);
        HttpStatus httpStatus = null;
        UserResponse body = new UserResponse();
        if(user!=null){
            httpStatus = HttpStatus.OK;
            setUserResponseBody(body, user);
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(body, httpStatus);
   }

   @PostMapping("/user/friend/{username}")
   public ResponseEntity<UserResponse> requestFriend(
           @RequestHeader(value = "Authorization")String accessTokenString,
           @PathVariable String username) {
        UserResponse body = userService.requestFriend(username, jwtProvider.getUsername(accessTokenString));
        HttpStatus httpStatus = null;
        if (body != null){
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(body, httpStatus);
   }

   @GetMapping("/user/friend")
   public ResponseEntity<ArrayList<FriendResponse>> getFriends(
           @RequestHeader(value = "Authorization")String accessTokenString) {
        ArrayList<FriendResponse> body = userService.getFriendList(jwtProvider.getUsername(accessTokenString));
        HttpStatus httpStatus = null;
        if (body != null){
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(body, httpStatus);
   }

   @DeleteMapping("/user/friend/{username}")
   public ResponseEntity<FriendResponse> deleteFriend(
           @RequestHeader(value = "Authorization")String accessTokenString,
           @PathVariable String username ){
        FriendResponse body = userService.deleteFriend(username, jwtProvider.getUsername(accessTokenString));
        HttpStatus httpStatus = null;
        if (body != null) {
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(body, httpStatus);
   }


   private void setUserResponseBody(UserResponse body, User user){
       body.setUsername(user.getUsername());
       body.setSchoolName(user.getSchoolName());
       body.setCreatedAt(user.getCreatedAt());
       body.setModifiedAt(user.getModifiedAt());
   }

}
