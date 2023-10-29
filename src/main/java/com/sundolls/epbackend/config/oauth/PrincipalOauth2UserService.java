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
import lombok.RequiredArgsConstructor;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
@PropertySource("/oauth.properties")
public class PrincipalOauth2UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    String kakaoUserInfoUrl;


    public User loadUser(String provider, String accessTokenString) throws OAuth2AuthenticationException, GeneralSecurityException, IOException {
        OAuth2UserInfo oAuth2UserInfo = null;


        if (provider.equals("kakao")) {
            URI uri = URI.create(kakaoUserInfoUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBearerAuth(accessTokenString);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    }
            );

            oAuth2UserInfo = new KakaoUserInfo(response.getBody());

        } else if (provider.equals("google")) {
            GoogleIdToken idToken = googleIdTokenVerifier.verify(accessTokenString);
            oAuth2UserInfo = new GoogleUserInfo(idToken.getPayload());

        } else {
            throw new OAuth2AuthenticationException("");
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String username = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();
        String profileUrl = oAuth2UserInfo.getProfileUrl();

        User user = null;
        if (userRepository.findByEmail(email).isEmpty()) {
             user = User.builder()
                    .id(provider + "_" + providerId)
                    .username(username)
                    .email(email)
                    .tag(TagMaker.makeTag(username, userRepository.findAllByUsernameOrderByTagAsc(username)))
                     .totalStudyTime(0)
                     .profileUrl(profileUrl)
                    .build();
            userRepository.save(user);
        } else {
             user = userRepository.findByEmail(email).get();
            if (!user.getUsername().equals(username))
                user.update(username, null, TagMaker.makeTag(username, userRepository.findAllByUsernameOrderByTagAsc(username)), profileUrl);
        }
        return user;
    }
}