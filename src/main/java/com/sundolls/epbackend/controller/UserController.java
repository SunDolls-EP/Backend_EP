package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.config.auth.PrincipalDetails;
import com.sundolls.epbackend.config.util.SearchOption;
import com.sundolls.epbackend.dto.request.StudyInfoRequest;
import com.sundolls.epbackend.dto.request.UserPatchRequest;
import com.sundolls.epbackend.dto.response.FriendResponse;
import com.sundolls.epbackend.dto.response.StudyInfoResponse;
import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @GetMapping("/random/list")
    @Operation(summary = "유저 랜덤 리스트")
    public ResponseEntity<List<UserResponse>> getRandomUserList(
            @RequestParam(name = "limit", defaultValue = "10") Integer limit
    ) {
        return userService.getRandomUserList(limit);
    }

    @PutMapping("")
    @Operation(summary = "유저 정보 수정")
    public ResponseEntity<UserResponse> updateUserInfo(
            @AuthenticationPrincipal @Parameter(hidden = true) User user,
            @RequestBody UserPatchRequest request){
        return userService.updateUser(request, user);
    }



    @GetMapping("/{username}")
    @Operation(summary = "이름으로 유저 찾기")
    public ResponseEntity<List<UserResponse>> findUser(@PathVariable String username){
        return userService.findUser(username);
   }

    @GetMapping("/{username}/{tag}")
    @Operation(summary = "이름과 태그로 유저 찾기")
    public ResponseEntity<UserResponse> findUser(
            @PathVariable String username,
            @PathVariable String tag
            ){
        return userService.findUser(username, tag);
    }


   @PostMapping("/friend/{username}/{tag}")
   @Operation(summary = "친구 요청")
   @Parameters({
           @Parameter(name = "username", description = "사용자이름", required = true),
           @Parameter(name = "tag", description = "사용자 태그", required = true)
   })
   public ResponseEntity<UserResponse> requestFriend(
           @AuthenticationPrincipal @Parameter(hidden = true) User user,
           @PathVariable String username,
           @PathVariable String tag) {
        return userService.requestFriend(username, tag, user);
   }

   @GetMapping("/friend")
   @Operation(summary = "친구 리스트 가져오기")
   public ResponseEntity<List<FriendResponse>> getFriends(
           @AuthenticationPrincipal @Parameter(hidden = true) User user) {
        return userService.getFriendList(user);
   }

   @DeleteMapping("/friend/{username}/{tag}")
   @Operation(summary = "친구 삭제")
   @Parameters({
           @Parameter(name = "username", description = "사용자이름", required = true),
           @Parameter(name = "tag", description = "사용자 태그", required = true)
   })
   public ResponseEntity<FriendResponse> deleteFriend(
           @AuthenticationPrincipal @Parameter(hidden = true) User user,
           @PathVariable String username,
           @PathVariable String tag) {
        return userService.deleteFriend(username, tag, user);
   }

   @PatchMapping("/friend/{username}/{tag}")
   @Operation(summary = "친구 요청 수락")
   @Parameters({
           @Parameter(name = "username", description = "사용자이름", required = true),
           @Parameter(name = "tag", description = "사용자 태그", required = true)
   })
   public ResponseEntity<FriendResponse> acceptFriend(
           @AuthenticationPrincipal @Parameter(hidden = true) User user,
           @PathVariable String username,
           @PathVariable String tag) {
        return userService.acceptFriend(username, tag, user);
   }

   @PostMapping("/study")
   @Operation(summary = "공부 기록 등록")
    public ResponseEntity<Void> postStudy(
           @AuthenticationPrincipal @Parameter(hidden = true) User user,
           @RequestBody StudyInfoRequest request
           ) {
        return userService.makeStudyInfo(user, request);
   }

   @GetMapping("/study/detail")
   @Operation(summary = "공부 기록 상세 조회")
   @Parameters({
           @Parameter(name = "from", description = "yyyy-mm-dd HH 형식으로 조회할 시작일 (기본값 2000-01-01 00)", required = true),
           @Parameter(name = "to", description = "yyyy-mm-dd HH 형식으로 조회할 종료일 (기본값 3000-12-31 23)", required = true)
   })
   public ResponseEntity<List<StudyInfoResponse>> getStudyInfo(
           @AuthenticationPrincipal @Parameter(hidden = true) User user,
           @RequestParam(defaultValue = "2000-01-01 00") String from,
           @RequestParam(defaultValue = "3000-12-31 23") String to) {
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
       return userService.getStudyInfos(user, LocalDateTime.parse(from, formatter), LocalDateTime.parse(to, formatter));
   }

    @GetMapping("/study")
    @Operation(summary = "공부 기록 조회")
    @Parameters({
            @Parameter(name = "option", description = "option설명: [day:하루, week: 일주일, month: 한 달, year: 일 년]")
    })
    public ResponseEntity<List<StudyInfoResponse>> getStudyInfo(
            @AuthenticationPrincipal @Parameter(hidden = true) User user,
            @RequestParam(name = "option") String  option) {
        return userService.getStudyInfos(user, SearchOption.valueOf(option));
    }

}
