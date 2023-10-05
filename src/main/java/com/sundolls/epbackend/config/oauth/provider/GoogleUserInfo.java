package com.sundolls.epbackend.config.oauth.provider;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleUserInfo implements OAuth2UserInfo{

    private final GoogleIdToken.Payload attributes;

    @Override
    public String getProviderId() {
        return attributes.getSubject();
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return attributes.getEmail();
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getProfileUrl() {
        return (String) attributes.get("picture");
    }


}

