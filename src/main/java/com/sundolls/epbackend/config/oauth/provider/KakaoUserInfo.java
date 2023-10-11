package com.sundolls.epbackend.config.oauth.provider;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo{
    private final Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) ((Map)attributes
                .get("kakao_account"))
                .get("email");
    }

    @Override
    public String getName() {
        return ((String) ((Map)((Map)attributes
                .get("kakao_account"))
                .get("profile"))
                .get("nickname"));
    }

    @Override
    public String getProfileUrl() {
        return ((String) ((Map)((Map)attributes
                .get("kakao_account"))
                .get("profile"))
                .get("profile_image_url"));
    }


}
