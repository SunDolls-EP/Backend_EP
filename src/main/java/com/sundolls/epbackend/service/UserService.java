package com.sundolls.epbackend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.sundolls.epbackend.dto.request.UserPatchRequest;
import com.sundolls.epbackend.dto.response.FriendResponse;
import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.entity.Friend;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.entity.primaryKey.FriendId;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.repository.FriendRepository;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final JwtProvider jwtProvider;

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


    public User updateUser(UserPatchRequest request, String userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user = optionalUser.get();
        log.info(request.getUsername()+" "+request.getSchoolName());
        user.update(request.getUsername(),request.getSchoolName());

        userRepository.save(user);
        log.info(user.getSchoolName()+" "+user.getUsername());
        return user;
    }

    public User findUser(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()) return null;
        return optionalUser.get();
    }

    public UserResponse requestFriend(String username, String userId){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            log.info("Target Not Found");
            return null;
        }
        User targetUser = optionalUser.get();

        optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            log.info("Requester Not Found");
            return null;
        }
        User requesterUser = optionalUser.get();

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

    public ArrayList<FriendResponse> getFriendList(String userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user = optionalUser.get();

        ArrayList<Friend> friends = new ArrayList<>();

        friends.addAll(friendRepository.findAllByUser(user));
        friends.addAll(friendRepository.findAllByTargetUser(user));

        ArrayList<FriendResponse> friendResponses = new ArrayList<>();

        for (Friend friend : friends){
            FriendResponse element = new FriendResponse();

            //friend의 user가 리소스 요청자와 같은가
            if(friend.getUser().getUsername().equals(user.getUsername())) {
                element.setUsername(friend.getTargetUser().getUsername());
                element.setSchoolName(friend.getTargetUser().getSchoolName());
            } else {
                element.setUsername(friend.getUser().getUsername());
                element.setSchoolName(friend.getUser().getSchoolName());
            }
            element.setAccepted(friend.isAccepted());
            element.setCreatedAt(friend.getCreatedAt());
            element.setModifiedAt(friend.getModifiedAt());
            friendResponses.add(element);
        }

        return friendResponses;

    }

    public FriendResponse deleteFriend(String username, String userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user = optionalUser.get();

        optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return null;
        }
        User targetUser = optionalUser.get();

        Optional<Friend> optionalFriend = friendRepository.findByUserAndTargetUser(user, targetUser);
        if (optionalFriend.isEmpty()){
            optionalFriend = friendRepository.findByUserAndTargetUser(targetUser, user);
        }
        if (optionalFriend.isEmpty()) return null;
        Friend friend = optionalFriend.get();

        FriendResponse friendResponse = new FriendResponse();
        friendResponse.setUsername(friend.getTargetUser().getUsername());
        friendResponse.setAccepted(friend.isAccepted());
        friendResponse.setSchoolName(friend.getTargetUser().getSchoolName());

        friendRepository.delete(friend);

        return friendResponse;

    }



}
