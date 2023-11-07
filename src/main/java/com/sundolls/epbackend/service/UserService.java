package com.sundolls.epbackend.service;

import com.sundolls.epbackend.config.oauth.PrincipalOauth2UserService;
import com.sundolls.epbackend.config.util.SearchOption;
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
import com.sundolls.epbackend.execption.CustomException;
import com.sundolls.epbackend.execption.ErrorCode;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.mapper.FriendMapper;
import com.sundolls.epbackend.mapper.StudyInfoMapper;
import com.sundolls.epbackend.mapper.UserMapper;
import com.sundolls.epbackend.repository.FriendRepository;
import com.sundolls.epbackend.repository.StudyInfoRepository;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final StudyInfoRepository studyInfoRepository;
    private final JwtProvider jwtProvider;
    public ResponseEntity<UserResponse> oauthLogin(String provider, String  tokenString) {
        HttpStatus status = HttpStatus.OK;

        User user = null;
        try {
            user = principalOauth2UserService.loadUser(provider, tokenString);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",jwtProvider.generateAccessToken(user.getUsername(), user.getTag()));
        headers.set("Refresh", jwtProvider.generateRefreshToken(user.getEmail()));
        UserResponse body = UserMapper.toDto(userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAIL)));

        return new ResponseEntity<>(body, headers, status);

    }

    public ResponseEntity<UserResponse> refreshLogin(String refreshTokenString) {
        Optional<User> optionalUser =  userRepository.findByEmail(jwtProvider.getPayload(refreshTokenString).getBody().getSubject());
        if (optionalUser.isEmpty() || jwtProvider.validateToken(refreshTokenString)) throw new CustomException(ErrorCode.LOGIN_FAIL);
        User user = optionalUser.get();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",jwtProvider.generateAccessToken(user.getUsername(), user.getTag()));
        UserResponse body = UserMapper.toDto(user);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(body);
    }

    public ResponseEntity<List<UserResponse>> getRandomUserList(Integer limit) {
        return ResponseEntity.ok(userRepository.findByRandom(limit).stream().map(UserMapper::toDto).toList());
    }


    @Transactional
    public ResponseEntity<UserResponse> updateUser(UserPatchRequest request, User user){

        user.update(request.getUsername(),request.getSchoolName(),
                TagMaker.makeTag(request.getUsername(),
                        userRepository.findAllByUsernameOrderByTagAsc(request.getUsername())
                ) //makeTag
                ,null
        );

        UserResponse body = UserMapper.toDto(user);
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
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        List<UserResponse> body = userList.stream().map(UserMapper::toDto).toList();
        return new ResponseEntity<>(body, status);
    }

    public ResponseEntity<UserResponse> findUser(String username, String tag) {
        HttpStatus status = HttpStatus.OK;

        User user = userRepository.findByUsernameAndTag(username, tag)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        UserResponse body = UserMapper.toDto(user);
        return new ResponseEntity<>(body, status);
    }

    public ResponseEntity<UserResponse> requestFriend(String targetUsername,String targetTag ,User requesterUser){

        User targetUser = userRepository.findByUsernameAndTag(targetUsername, targetTag)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

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

        UserResponse body = UserMapper.toDto(targetUser);

        return ResponseEntity.ok(body);

    }

    public ResponseEntity<List<FriendResponse>> getFriendList(User user){

        List<Friend> friends = new ArrayList<>();

        friends.addAll(friendRepository.findAllByUser(user));
        friends.addAll(friendRepository.findAllByTargetUser(user));

        log.info(friends.toString());

        List<FriendResponse> body = friends.stream().map( friend -> FriendMapper.toDto(friend, user)  ).toList();

        return ResponseEntity.ok(body);

    }

    public ResponseEntity<FriendResponse> deleteFriend(String username, String tag, User user){
        User targetUser = userRepository.findByUsernameAndTag(username, tag)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Friend friend = friendRepository.findByUserAndTargetUser(user, targetUser)
                .orElse(friendRepository.findByUserAndTargetUser(targetUser, user)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
                );

        FriendResponse body = FriendMapper.toDto(friend, user);

        friendRepository.delete(friend);

        return ResponseEntity.ok(body);

    }

    public ResponseEntity<FriendResponse> acceptFriend(String username, String tag, User user){
        User targetUser = userRepository.findByUsernameAndTag(username, tag)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Friend friend = friendRepository.findByUserAndTargetUser(user, targetUser)
                .orElse(friendRepository.findByUserAndTargetUser(targetUser, user)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
                );

        if (! friend.getTargetUser().equals(user)){
            throw new CustomException(ErrorCode.FRIEND_FORBIDDEN);
        }

        friend.accept();
        friendRepository.save(friend);

        FriendResponse body = FriendMapper.toDto(friend, user);

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
        List<StudyInfoResponse> body = studyInfos.stream().map(StudyInfoMapper::toDto).toList();
        if (body.isEmpty()) throw new CustomException(ErrorCode.STUDY_INFO_NOT_FOUND);

        return ResponseEntity.ok(body);
    }

    public ResponseEntity<List<StudyInfoResponse>> getStudyInfos(User user, SearchOption searchOption) {
        Calendar cal = Calendar.getInstance();

        List<StudyInfo> studyInfos;
        if (searchOption == SearchOption.DAY) {
            LocalDateTime from = LocalDateTime.of(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            studyInfos =  studyInfoRepository.findByUserAndCreatedAtBetween(user, from, LocalDateTime.now());

        } else if (searchOption == SearchOption.WEEK) {
            LocalDateTime from = LocalDateTime.of(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1, cal.getFirstDayOfWeek(), 0, 0, 0);
            studyInfos =  studyInfoRepository.findByUserAndCreatedAtBetween(user, from, LocalDateTime.now());

        } else if (searchOption == SearchOption.MONTH) {
            LocalDateTime from = LocalDateTime.of(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1, 1, 0, 0, 0);
            studyInfos =  studyInfoRepository.findByUserAndCreatedAtBetween(user, from, LocalDateTime.now());
        } else if (searchOption == SearchOption.YEAR) {
            LocalDateTime from = LocalDateTime.of(cal.get(Calendar.YEAR),1, 1, 0, 0, 0);
            studyInfos =  studyInfoRepository.findByUserAndCreatedAtBetween(user, from, LocalDateTime.now());
        } else {
            studyInfos = studyInfoRepository.findByUser(user);
        }

        List<StudyInfoResponse> body = studyInfos.stream().map(StudyInfoMapper::toDto).toList();
        if (body.isEmpty()) throw new CustomException(ErrorCode.STUDY_INFO_NOT_FOUND);

        return ResponseEntity.ok(body);
    }

}
