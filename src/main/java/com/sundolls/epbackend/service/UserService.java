package com.sundolls.epbackend.service;

import com.sundolls.epbackend.config.oauth.PrincipalOauth2UserService;
import com.sundolls.epbackend.config.util.TagMaker;
import com.sundolls.epbackend.dto.request.StudyInfoRequest;
import com.sundolls.epbackend.dto.request.UserPatchRequest;
import com.sundolls.epbackend.dto.response.FriendResponse;
import com.sundolls.epbackend.dto.response.StudyInfoResponse;
import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.entity.Friend;
import com.sundolls.epbackend.entity.StudyInfo;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.entity.primaryKey.FriendId;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.mapper.FriendMapper;
import com.sundolls.epbackend.mapper.StudyInfoMapper;
import com.sundolls.epbackend.mapper.UserMapper;
import com.sundolls.epbackend.repository.FriendRepository;
import com.sundolls.epbackend.repository.StudyInfoRepository;
import com.sundolls.epbackend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final StudyInfoRepository studyInfoRepository;
    private final JwtProvider jwtProvider;
    private final FriendMapper friendMapper;
    private final StudyInfoMapper studyInfoMapper;
    private final UserMapper userMapper;

    public ResponseEntity<UserResponse> oauthLogin(String provider, String  tokenString) {
        HttpStatus status = HttpStatus.OK;

        User user = null;
        try {
            user = principalOauth2UserService.loadUser(provider, tokenString);
        } catch (Exception e) {
            status = HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(status);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",jwtProvider.generateAccessToken(user.getUsername(), user.getTag()));
        headers.set("Refresh", jwtProvider.generateRefreshToken(user.getEmail()));
        UserResponse body = userMapper.toDto(user);

        return new ResponseEntity<>(body, headers, status);

    }

    public ResponseEntity<UserResponse> refreshLogin(String refreshTokenString) {
        Optional<User> optionalUser =  userRepository.findByEmail(jwtProvider.getPayload(refreshTokenString).getBody().getSubject());
        if (optionalUser.isEmpty() || jwtProvider.validateToken(refreshTokenString)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        User user = optionalUser.get();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",jwtProvider.generateAccessToken(user.getUsername(), user.getTag()));
        UserResponse body = userMapper.toDto(user);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(body);
    }

    public ResponseEntity<List<UserResponse>> getRandomUserList(Integer limit) {
        return ResponseEntity.ok(userRepository.findByRandom(limit).stream().map(userMapper::toDto).toList());
    }


    @Transactional
    public ResponseEntity<UserResponse> updateUser(UserPatchRequest request, User user){

        user.update(request.getUsername(),request.getSchoolName(),
                TagMaker.makeTag(request.getUsername(),
                        userRepository.findAllByUsernameOrderByTagAsc(request.getUsername())
                ) //makeTag
                ,null
        );

        UserResponse body = userMapper.toDto(user);
        if (request.getUsername() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization",jwtProvider.generateAccessToken(user.getUsername(), user.getTag()));
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(body);
        }

        return ResponseEntity.ok(body);
    }


    public ResponseEntity<List<UserResponse>> findUser(String username){
        HttpStatus status = HttpStatus.OK;

        List<User> userList = userRepository.findByUsername(username);
        if(userList.isEmpty()){
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        }
        List<UserResponse> body = userList.stream().map(userMapper::toDto).toList();
        return new ResponseEntity<>(body, status);
    }

    public ResponseEntity<UserResponse> findUser(String username, String tag) {
        HttpStatus status = HttpStatus.OK;

        Optional<User> optionalUser = userRepository.findByUsernameAndTag(username, tag);
        if(optionalUser.isEmpty()){
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        }
        UserResponse body = userMapper.toDto(optionalUser.get());
        return new ResponseEntity<>(body, status);
    }

    public ResponseEntity<UserResponse> requestFriend(String targetUsername,String targetTag ,User requesterUser){

        Optional<User> optionalUser = userRepository.findByUsernameAndTag(targetUsername, targetTag);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User targetUser = optionalUser.get();


        FriendId friendId = new FriendId();
        friendId.setUserId(requesterUser.getId());
        friendId.setTargetId(targetUser.getId());

        Friend friend = Friend.builder()
                .friendId(friendId)
                .user(requesterUser)
                .targetUser(targetUser)
                .accepted(false)
                .build();
        friendRepository.save(friend);

        UserResponse body = userMapper.toDto(targetUser);

        return ResponseEntity.ok(body);

    }

    public ResponseEntity<List<FriendResponse>> getFriendList(User user){

        List<Friend> friends = new ArrayList<>();

        friends.addAll(friendRepository.findAllByUser(user));
        friends.addAll(friendRepository.findAllByTargetUser(user));

        log.info(friends.toString());

        List<FriendResponse> body = friends.stream().map( friend -> friendMapper.toDto(friend, user)  ).toList();

        return ResponseEntity.ok(body);

    }

    public ResponseEntity<FriendResponse> deleteFriend(String username, String tag, User user){
        User targetUser = userRepository.findByUsernameAndTag(username, tag).get();

        Optional<Friend> optionalFriend = friendRepository.findByUserAndTargetUser(user, targetUser);
        if (optionalFriend.isEmpty()){
            optionalFriend = friendRepository.findByUserAndTargetUser(targetUser, user);
        }
        if (optionalFriend.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Friend friend = optionalFriend.get();

        FriendResponse body = friendMapper.toDto(friend, user);

        friendRepository.delete(friend);

        return ResponseEntity.ok(body);

    }

    public ResponseEntity<FriendResponse> acceptFriend(String username, String tag, User user){
        User targetUser = userRepository.findByUsernameAndTag(username, tag).get();

        Optional<Friend> optionalFriend = friendRepository.findByUserAndTargetUser(user, targetUser);
        if (optionalFriend.isEmpty()){
            optionalFriend = friendRepository.findByUserAndTargetUser(targetUser, user);
        }
        if (optionalFriend.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Friend friend = optionalFriend.get();

        if (! friend.getTargetUser().equals(user)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        friend.accept();
        friendRepository.save(friend);

        FriendResponse body = friendMapper.toDto(friend, user);

        return ResponseEntity.ok(body);
    }

    @Transactional
    public ResponseEntity<Void> makeStudyInfo(User user, StudyInfoRequest request) {

        user.addTime(request.getTotalStudyTime());

        StudyInfo studyInfo = StudyInfo.builder()
                .user(user)
                .createdAt(request.getStartAt())
                .time(request.getTotalStudyTime())
                .build();
        studyInfoRepository.save(studyInfo);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<StudyInfoResponse>> getStudyInfos(User user, LocalDateTime from, LocalDateTime to) {

        List<StudyInfo> studyInfos =  studyInfoRepository.findByUserAndCreatedAtBetween(user, from, to);
        List<StudyInfoResponse> body = studyInfos.stream().map(studyInfoMapper::toDto).toList();
        if (body.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(body);
    }


}
