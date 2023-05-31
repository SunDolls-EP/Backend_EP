package com.sundolls.epbackend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.sundolls.epbackend.dto.request.StudyInfoRequest;
import com.sundolls.epbackend.dto.request.UserPatchRequest;
import com.sundolls.epbackend.dto.response.FriendResponse;
import com.sundolls.epbackend.dto.response.StudyInfoResponse;
import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.entity.Friend;
import com.sundolls.epbackend.entity.StudyInfo;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.entity.primaryKey.FriendId;
import com.sundolls.epbackend.mapper.StudyInfoMapper;
import com.sundolls.epbackend.repository.FriendRepository;
import com.sundolls.epbackend.repository.StudyInfoRepository;
import com.sundolls.epbackend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Time;
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

    public User join(String  idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);
        if(idToken!=null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String id = payload.getSubject();
            String username = (String) payload.get("name");
            String email = payload.getEmail();

            User user = null;
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                user = User.builder()
                        .id(id)
                        .username(username)
                        .password(null)
                        .email(email)
                        .build();
                userRepository.save(user);
            }
            else{
                user = optionalUser.get();
            }
            return user;
        }
        return null;
    }


    public User updateUser(UserPatchRequest request, Jws<Claims> payload){
        User user = getUser(payload);

        user.update(request.getUsername(),request.getSchoolName());

        userRepository.save(user);
        return user;
    }

    public User findUser(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()) return null;
        return optionalUser.get();
    }

    public UserResponse requestFriend(String username,Jws<Claims> payload){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            log.info("Target Not Found");
            return null;
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

        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(targetUser.getUsername());
        userResponse.setSchoolName(targetUser.getSchoolName());
        userResponse.setCreatedAt(targetUser.getCreatedAt());
        userResponse.setModifiedAt(targetUser.getModifiedAt());

        return userResponse;

    }

    public ArrayList<FriendResponse> getFriendList(Jws<Claims> payload){
        User user = getUser(payload);

        ArrayList<Friend> friends = new ArrayList<>();

        friends.addAll(friendRepository.findAllByUser(user));
        friends.addAll(friendRepository.findAllByTargetUser(user));

        ArrayList<FriendResponse> friendResponses = new ArrayList<>();

        for (Friend friend : friends){
            FriendResponse element = makeResponse(user, friend);
            friendResponses.add(element);
        }

        return friendResponses;

    }

    public FriendResponse deleteFriend(String username,Jws<Claims> payload){
        User user = getUser(payload);
        User targetUser = getTarget(username);
        if(user==null || targetUser == null) return null;

        Optional<Friend> optionalFriend = friendRepository.findByUserAndTargetUser(user, targetUser);
        if (optionalFriend.isEmpty()){
            optionalFriend = friendRepository.findByUserAndTargetUser(targetUser, user);
        }
        if (optionalFriend.isEmpty()) return null;
        Friend friend = optionalFriend.get();

        FriendResponse friendResponse = makeResponse(user, friend);

        friendRepository.delete(friend);

        return friendResponse;

    }

    public FriendResponse acceptFriend(String username, Jws<Claims> payload){
        User user = getUser(payload);
        User targetUser = getTarget(username);
        if(user==null || targetUser == null) return null;

        Optional<Friend> optionalFriend = friendRepository.findByUserAndTargetUser(user, targetUser);
        if (optionalFriend.isEmpty()){
            optionalFriend = friendRepository.findByUserAndTargetUser(targetUser, user);
        } else {
            return makeResponse(user, optionalFriend.get());
        }
        if (optionalFriend.isEmpty()) return null;
        Friend friend = optionalFriend.get();

        friend.accept();
        friendRepository.save(friend);

        return makeResponse(user, friend);
    }

    public ResponseEntity<Void> postStudyInfo(Jws<Claims> payload, StudyInfoRequest request) {
        HttpStatus status = HttpStatus.OK;

        User user = getUser(payload);
        if (user==null) {
            status = HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(status);
        }

        StudyInfo studyInfo = StudyInfo.builder()
                .user(user)
                .createdAt(request.getStartAt())
                .time(Duration.between(request.getEndAt(), request.getStartAt()).getSeconds())
                .build();
        studyInfoRepository.save(studyInfo);

        return new ResponseEntity<>(status);
    }

    public ResponseEntity<List<StudyInfoResponse>> getStudyInfos(LocalDateTime from, LocalDateTime to) {
        HttpStatus status = HttpStatus.OK;

        List<StudyInfo> studyInfos =  studyInfoRepository.findByCreatedAtBetween(from, to);
        List<StudyInfoResponse> body = studyInfos.stream().map(StudyInfoMapper.MAPPER::toDto).toList();
        if (body.isEmpty()) status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(body, status);
    }




    private User getUser(Jws<Claims> payload){
        Optional<User> optionalUser = userRepository.findByUsernameAndTag((String) payload.getBody().get("username"), (String) payload.getBody().get("tag"));
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        return user;
    }

    private User getTarget(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        User target = null;
        if (optionalUser.isPresent()) {
            target = optionalUser.get();
        }
        return target;
    }

    private FriendResponse makeResponse(User user, Friend friend){
        FriendResponse friendResponse = new FriendResponse();
        if(friend.getUser().getUsername().equals(user.getUsername())) {
            friendResponse.setUsername(friend.getTargetUser().getUsername());
            friendResponse.setSchoolName(friend.getTargetUser().getSchoolName());
        } else {
            friendResponse.setUsername(friend.getUser().getUsername());
            friendResponse.setSchoolName(friend.getUser().getSchoolName());
        }
        friendResponse.setAccepted(friend.isAccepted());
        friendResponse.setCreatedAt(friend.getCreatedAt());
        friendResponse.setModifiedAt(friend.getModifiedAt());

        return friendResponse;
    }

}
