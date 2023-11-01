package com.sundolls.epbackend.config.oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.sundolls.epbackend.config.auth.PrincipalDetails;
import com.sundolls.epbackend.config.oauth.provider.GoogleUserInfo;
import com.sundolls.epbackend.config.oauth.provider.KakaoUserInfo;
import com.sundolls.epbackend.config.oauth.provider.OAuth2UserInfo;
import com.sundolls.epbackend.config.util.TagMaker;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.repository.UserRepository;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@PropertySource("/oauth.properties")
@Slf4j
public class PrincipalOauth2UserService {

    private final UserRepository userRepository;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.nonce}")
    String kakaoNonce;


    public User loadUser(String provider, String idTokenString) {
        OAuth2UserInfo oAuth2UserInfo = null;


        if (provider.equals("kakao")) {
            String[] chunks = idTokenString.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));
            payload = payload.substring(1, payload.length() - 1);
            String[] payloadArr = payload.split(",");
            Map<String, Object> kakaoPayload = new HashMap<>();
            for (String str : payloadArr ) {
                kakaoPayload.put((str.split(":", 2)[0]).replaceAll("\"","") , (str.split(":", 2)[1]).replaceAll("\"",""));
            }
            if (
                    ((String) kakaoPayload.get("iss")).equals("https://kauth.kakao.com")
                            &&
                    ((String)kakaoPayload.get("aud")).equals(kakaoClientId)
                            &&
                    (Long.parseLong((String) kakaoPayload.get("exp")) > (System.currentTimeMillis() / 1000))
                            &&
                    ((String)kakaoPayload.get("nonce")).equals(kakaoNonce)
            ) {
                oAuth2UserInfo = new KakaoUserInfo(kakaoPayload);
            } else {
                throw new OAuth2AuthenticationException("kakao Id Token validation failed.");
            }

        } else if (provider.equals("google")) {
            try {

                GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);
                oAuth2UserInfo = new GoogleUserInfo(idToken.getPayload());
            } catch (GeneralSecurityException | IOException e) {
            }

        } else {
            throw new OAuth2AuthenticationException("unknown oauth provider.");
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String username = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();
        String profileUrl = oAuth2UserInfo.getProfileUrl();

        User user = null;
        if (userRepository.findById(provider + "_" + providerId).isEmpty()) {
             user = User.builder()
                    .id(provider + "_" + providerId)
                    .username(username)
                    .email(email)
                    .tag(TagMaker.makeTag(username, userRepository.findAllByUsernameOrderByTagAsc(username)))
                     .totalStudyTime(0)
                     .profileUrl(profileUrl)
                    .build();
            userRepository.save(user);
        } else{
             user = userRepository.findById(provider + "_" + providerId).get();
        }
        return user;
    }
}