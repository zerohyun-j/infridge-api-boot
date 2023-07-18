package com.inthefridges.api.global.security.oauth.user;

import java.util.Map;

public class GoogleUser extends OAuthUserInfo{

    public GoogleUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProvider() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getProfileImageUrl() {
        return null;
    }
}
