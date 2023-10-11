package com.sundolls.epbackend.controller;

import com.sundolls.epbackend.config.oauth.PrincipalOauth2UserService;
import com.sundolls.epbackend.dto.request.StudyInfoRequest;
import com.sundolls.epbackend.dto.request.UserPatchRequest;
import com.sundolls.epbackend.dto.response.FriendResponse;
import com.sundolls.epbackend.dto.response.StudyInfoResponse;
import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final JwtProvider jwtProvider;

    /**
    @GetMapping("/login/oauth2/google")
    public ResponseEntity<UserResponse> googleLogin(
            @RequestHeader(value = "Authorization")String idTokenString ) throws GeneralSecurityException, IOException {
        return userService.join(idTokenString);
    }
     **/

    @GetMapping("/login/oauth2/authorize/{provider}")
    @Operation(summary = "Oauth2 로그인")
    @Parameter(name = "provider", description = "Oauth2 프로바이더 (google, kakao)", required = true)
    public ResponseEntity<UserResponse> oauthLogin(
            @PathVariable(name = "provider") String provider,
            @RequestHeader(value = "Authorization")String idTokenString) {

        return userService.oauthLogin(provider, idTokenString);
    }

    @PutMapping("/api/user")
    @Operation(summary = "유저 정보 수정")
    public ResponseEntity<UserResponse> updateUserInfo(
            @RequestHeader(value = "Authorization")String accessTokenString,
            @RequestBody UserPatchRequest request){
        return userService.updateUser(request, jwtProvider.getPayload(accessTokenString));
    }



    @GetMapping("/api/user/{username}")
    @Operation(summary = "이름으로 유저 찾기")
    public ResponseEntity<UserResponse> findUser(@PathVariable String username){
        return userService.findUser(username);
   }

    @GetMapping("/api/user/{username}/{tag}")
    @Operation(summary = "이름과 태그로 유저 찾기")
    public ResponseEntity<UserResponse> findUser(
            @PathVariable String username,
            @PathVariable String tag
            ){
        return userService.findUser(username, tag);
    }

   @PostMapping("/api/user/friend/{username}/{tag}")
   @Operation(summary = "친구 요청")
   @Parameters({
           @Parameter(name = "username", description = "사용자이름", required = true),
           @Parameter(name = "tag", description = "사용자 태그", required = true)
   })
   public ResponseEntity<UserResponse> requestFriend(
           @RequestHeader(value = "Authorization")String accessTokenString,
           @PathVariable String username,
           @PathVariable String tag) {
        return userService.requestFriend(username, tag, jwtProvider.getPayload(accessTokenString));
   }

   @GetMapping("/api/user/friend")
   @Operation(summary = "친구 리스트 가져오기")
   public ResponseEntity<List<FriendResponse>> getFriends(
           @RequestHeader(value = "Authorization")String accessTokenString) {
        return userService.getFriendList(jwtProvider.getPayload(accessTokenString));
   }

   @DeleteMapping("/api/user/friend/{username}/{tag}")
   @Operation(summary = "친구 삭제")
   @Parameters({
           @Parameter(name = "username", description = "사용자이름", required = true),
           @Parameter(name = "tag", description = "사용자 태그", required = true)
   })
   public ResponseEntity<FriendResponse> deleteFriend(
           @RequestHeader(value = "Authorization")String accessTokenString,
           @PathVariable String username,
           @PathVariable String tag) {
        return userService.deleteFriend(username, tag, jwtProvider.getPayload(accessTokenString));
   }

   @PatchMapping("/api/user/friend/{username}/{tag}")
   @Operation(summary = "친구 요청 수락")
   @Parameters({
           @Parameter(name = "username", description = "사용자이름", required = true),
           @Parameter(name = "tag", description = "사용자 태그", required = true)
   })
   public ResponseEntity<FriendResponse> acceptFriend(
           @RequestHeader(value = "Authorization")String accessTokenString,
           @PathVariable String username,
           @PathVariable String tag) {
        return userService.acceptFriend(username, tag,jwtProvider.getPayload(accessTokenString));
   }

   @PostMapping("/api/user/study")
   @Operation(summary = "공부 기록 등록")
    public ResponseEntity<Void> postStudy(
           @RequestHeader(value = "Authorization")String accessTokenString,
           @RequestBody StudyInfoRequest request
           ) {
        return userService.makeStudyInfo(jwtProvider.getPayload(accessTokenString), request);
   }

   @GetMapping("/api/user/study")
   @Operation(summary = "공부 기록 조회")
   @Parameters({
           @Parameter(name = "from", description = "yyyy-mm-dd HH 형식으로 조회할 시작일 (기본값 2000-01-01 00)", required = true),
           @Parameter(name = "to", description = "yyyy-mm-dd HH 형식으로 조회할 종료일 (기본값 3000-12-31 23)", required = true)
   })
   public ResponseEntity<List<StudyInfoResponse>> getStudyInfo(
           @RequestHeader(value = "Authorization")String accessTokenString,
           @RequestParam(defaultValue = "2000-01-01 00") String from,
           @RequestParam(defaultValue = "3000-12-31 23") String to) {
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
       return userService.getStudyInfos(jwtProvider.getPayload(accessTokenString), LocalDateTime.parse(from, formatter), LocalDateTime.parse(to, formatter));
   }

}
