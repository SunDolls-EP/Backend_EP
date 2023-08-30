package com.sundolls.epbackend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
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
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final StudyInfoRepository studyInfoRepository;
    private final JwtProvider jwtProvider;
    private final FriendMapper friendMapper;
    private final StudyInfoMapper studyInfoMapper;
    private final UserMapper userMapper;

    public ResponseEntity<UserResponse> join(String  idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);
        HttpStatus status = HttpStatus.OK;

        if(idToken!=null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String id = payload.getSubject();
            String username = (String) payload.get("name");
            String email = payload.getEmail();

            Optional<User> optionalUser = userRepository.findByEmail(email);
            User user = null;
            if (optionalUser.isEmpty()) {
                user = User.builder()
                        .id("google_" + id)
                        .username(username)
                        .tag(TagMaker.makeTag(username, userRepository.findAllByUsernameOrderByTagAsc(username)))
                        .password(null)
                        .email(email)
                        .totalStudyTime(0)
                        .build();
                userRepository.save(user);
            } else {
                user = optionalUser.get();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization",jwtProvider.generateToken(user.getUsername(), user.getTag()));
            UserResponse body = userMapper.toDto(user);

            return new ResponseEntity<>(body, headers, status);
        }
        status = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(status);
    }

    @Transactional
    public ResponseEntity<UserResponse> updateUser(UserPatchRequest request, Jws<Claims> payload){
        HttpStatus status = HttpStatus.OK;

        User user = getUser(payload);

        user.update(request.getUsername(),request.getSchoolName(),
                TagMaker.makeTag(request.getUsername(),
                        userRepository.findAllByUsernameOrderByTagAsc(request.getUsername())
                ) //makeTag
        );

        UserResponse body = userMapper.toDto(user);
        if (request.getUsername() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization",jwtProvider.generateToken(user.getUsername(), user.getTag()));
            return new ResponseEntity<>(body, headers ,status);
        }

        return new ResponseEntity<>(body, status);
    }

    public ResponseEntity<UserResponse> findUser(String username){
        HttpStatus status = HttpStatus.OK;

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        }
        UserResponse body = userMapper.toDto(optionalUser.get());
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

    public ResponseEntity<UserResponse> requestFriend(String targetUsername,String targetTag ,Jws<Claims> payload){
        HttpStatus httpStatus = HttpStatus.OK;

        Optional<User> optionalUser = userRepository.findByUsernameAndTag(targetUsername, targetTag);
        if (optionalUser.isEmpty()) {
            httpStatus = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(httpStatus);
        }
        User targetUser = optionalUser.get();

        User requesterUser = getUser(payload);

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

        return new ResponseEntity<>(body, httpStatus);

    }

    public ResponseEntity<List<FriendResponse>> getFriendList(Jws<Claims> payload){
        HttpStatus status = HttpStatus.OK;

        User user = getUser(payload);

        List<Friend> friends = new ArrayList<>();

        friends.addAll(friendRepository.findAllByUser(user));
        friends.addAll(friendRepository.findAllByTargetUser(user));

        log.info(friends.toString());

        List<FriendResponse> body = friends.stream().map( friend -> friendMapper.toDto(friend, user)  ).toList();

        return new ResponseEntity<>(body, status);

    }

    public ResponseEntity<FriendResponse> deleteFriend(String username, String tag, Jws<Claims> payload){
        HttpStatus status = HttpStatus.OK;

        User user = getUser(payload);
        User targetUser = userRepository.findByUsernameAndTag(username, tag).get();

        Optional<Friend> optionalFriend = friendRepository.findByUserAndTargetUser(user, targetUser);
        if (optionalFriend.isEmpty()){
            optionalFriend = friendRepository.findByUserAndTargetUser(targetUser, user);
        }
        if (optionalFriend.isEmpty()){
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        }
        Friend friend = optionalFriend.get();

        FriendResponse body = friendMapper.toDto(friend, user);

        friendRepository.delete(friend);

        return new ResponseEntity<>(body, status);

    }

    public ResponseEntity<FriendResponse> acceptFriend(String username, String tag, Jws<Claims> payload){
        HttpStatus status = HttpStatus.OK;

        User user = getUser(payload);
        User targetUser = userRepository.findByUsernameAndTag(username, tag).get();

        Optional<Friend> optionalFriend = friendRepository.findByUserAndTargetUser(user, targetUser);
        if (optionalFriend.isEmpty()){
            optionalFriend = friendRepository.findByUserAndTargetUser(targetUser, user);
        }
        if (optionalFriend.isEmpty()){
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(status);
        }
        Friend friend = optionalFriend.get();

        if (! friend.getTargetUser().equals(user)){
            status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<>(status);
        }

        friend.accept();
        friendRepository.save(friend);

        FriendResponse body = friendMapper.toDto(friend, user);

        return new ResponseEntity<>(body, status);
    }

    @Transactional
    public ResponseEntity<Void> makeStudyInfo(Jws<Claims> payload, StudyInfoRequest request) {
        HttpStatus status = HttpStatus.OK;

        User user = getUser(payload);
        user.addTime(request.getTotalStudyTime());

        StudyInfo studyInfo = StudyInfo.builder()
                .user(user)
                .createdAt(request.getStartAt())
                .time(request.getTotalStudyTime())
                .build();
        studyInfoRepository.save(studyInfo);

        return new ResponseEntity<>(status);
    }

    public ResponseEntity<List<StudyInfoResponse>> getStudyInfos(Jws<Claims> payload, LocalDateTime from, LocalDateTime to) {
        HttpStatus status = HttpStatus.OK;

        User user = getUser(payload);

        List<StudyInfo> studyInfos =  studyInfoRepository.findByUserAndCreatedAtBetween(user, from, to);
        List<StudyInfoResponse> body = studyInfos.stream().map(studyInfoMapper::toDto).toList();
        if (body.isEmpty()) status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(body, status);
    }


    private User getUser(Jws<Claims> payload){
        Optional<User> optionalUser = userRepository.findByUsernameAndTag((String) payload.getBody().get("username"), (String) payload.getBody().get("tag"));
        if (optionalUser.isEmpty()) {
            return null;
        }
        return optionalUser.get();
    }

}
