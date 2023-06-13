package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.dto.request.StudyInfoRequest;
import com.sundolls.epbackend.dto.request.UserPatchRequest;
import com.sundolls.epbackend.dto.response.FriendResponse;
import com.sundolls.epbackend.dto.response.StudyInfoResponse;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        return userService.join(idTokenString);
    }

    @PutMapping("/user")
    public ResponseEntity<UserResponse> updateUserInfo(
            @RequestHeader(value = "Authorization")String accessTokenString,
            @RequestBody UserPatchRequest request){
        return userService.updateUser(request, jwtProvider.getPayload(accessTokenString));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<UserResponse> findUser(@PathVariable String username){
        return userService.findUser(username);
   }

    @GetMapping("/user/{username}/{tag}")
    public ResponseEntity<UserResponse> findUser(
            @PathVariable String username,
            @PathVariable String tag
            ){
        return userService.findUser(username, tag);
    }

   @PostMapping("/user/friend/{username}/{tag}")
   public ResponseEntity<UserResponse> requestFriend(
           @RequestHeader(value = "Authorization")String accessTokenString,
           @PathVariable String username,
           @PathVariable String tag) {
        return userService.requestFriend(username, tag, jwtProvider.getPayload(accessTokenString));
   }

   @GetMapping("/user/friend")
   public ResponseEntity<List<FriendResponse>> getFriends(
           @RequestHeader(value = "Authorization")String accessTokenString) {
        log.info("here");
        return userService.getFriendList(jwtProvider.getPayload(accessTokenString));
   }

   @DeleteMapping("/user/friend/{username}/{tag}")
   public ResponseEntity<FriendResponse> deleteFriend(
           @RequestHeader(value = "Authorization")String accessTokenString,
           @PathVariable String username,
           @PathVariable String tag) {
        return userService.deleteFriend(username, tag, jwtProvider.getPayload(accessTokenString));
   }

   @PatchMapping("/user/friend/{username}/{tag}")
   public ResponseEntity<FriendResponse> acceptFriend(
           @RequestHeader(value = "Authorization")String accessTokenString,
           @PathVariable String username,
           @PathVariable String tag) {
        return userService.acceptFriend(username, tag,jwtProvider.getPayload(accessTokenString));
   }

   @PostMapping("/user/study")
    public ResponseEntity<Void> postStudy(
           @RequestHeader(value = "Authorization")String accessTokenString,
           @RequestBody StudyInfoRequest request
           ) {
        return userService.postStudyInfo(jwtProvider.getPayload(accessTokenString), request);
   }

   @GetMapping("/user/study")
   public ResponseEntity<List<StudyInfoResponse>> getStudyInfo(
           @RequestHeader(value = "Authorization")String accessTokenString,
           @RequestParam(defaultValue = "2000-01-01 00") String from,
           @RequestParam(defaultValue = "3000-12-31 23") String to) {
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
       return userService.getStudyInfos(LocalDateTime.parse(from, formatter), LocalDateTime.parse(to, formatter));
   }


   private void setUserResponseBody(UserResponse body, User user){
       body.setUsername(user.getUsername());
       body.setSchoolName(user.getSchoolName());
       body.setCreatedAt(user.getCreatedAt());
       body.setModifiedAt(user.getModifiedAt());
   }

}
