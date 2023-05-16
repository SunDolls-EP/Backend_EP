package com.sundolls.epbackend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.sundolls.epbackend.dto.request.UserPatchRequest;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
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


    public User updateUser(UserPatchRequest request, String accessToken){
        Optional<User> optionalUser = userRepository.findById(jwtProvider.getUsername(accessToken));
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

}
