package com.sundolls.epbackend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.sundolls.epbackend.dto.UserDto;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final FirebaseAuth firebaseAuth;

    @Transactional
    public User register(String authorization, UserDto userDto){
        FirebaseToken decodedToken;
        try {
            if(authorization == null || !authorization.startsWith("Bearer ")){
                throw new IllegalArgumentException("Invalid authorization header");
            }
            String token = authorization.replace("Bearer ","");
            decodedToken = firebaseAuth.verifyIdToken(token);
        } catch (IllegalArgumentException | FirebaseAuthException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "{\"code\":\"INVALID_TOKEN\", \"message\":\"" + e.getMessage() + "\"}");
        }
        User user = User.builder()
                .uid(decodedToken.getUid())
                .email(decodedToken.getEmail())
                .nickname(userDto.getNickname())
                .school(userDto.getSchool())
                .build();
        userRepository.save(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        return userRepository.findById(uid).get();
    }
}
