package com.sundolls.epbackend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.sundolls.epbackend.dto.request.JoinRequest;
import com.sundolls.epbackend.dto.request.LoginRequest;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public User join(String  idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString.replace("Bearer ",""));
        GoogleIdToken.Payload payload = idToken.getPayload();
        String id = payload.getSubject();
        String username;
        String email = payload.getEmail();
        String providerId;

        User user = null;
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            user = User.builder()
                    .id(id)
                    .username(username)
                    .password(null)
                    .email(email)
                    .school(null)
                    .provider("google")
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        }
        return null;
    }
}
