package com.sundolls.epbackend.config.oauth;

import com.sundolls.epbackend.config.auth.PrincipalDetails;
import com.sundolls.epbackend.config.oauth.provider.KakaoUserInfo;
import com.sundolls.epbackend.config.oauth.provider.OAuth2UserInfo;
import com.sundolls.epbackend.config.util.TagMaker;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("");
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new OAuth2AuthenticationException("");
        }
        User user = User.builder()
                .id(provider + "_" + providerId)
                .username(username)
                .email(email)
                .tag(TagMaker.makeTag(username, userRepository.findAllByUsernameOrderByTagAsc(username)))
                .build();
        userRepository.save(user);

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
