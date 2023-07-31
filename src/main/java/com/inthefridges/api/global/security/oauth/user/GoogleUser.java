package com.inthefridges.api.global.security.oauth.user;

import java.util.Map;

public class GoogleUser extends OAuthUserInfo{

    public GoogleUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getId() { return (String)attributes.get("sub"); }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }

    @Override
    public String getProfileImageUrl() {
        return (String)attributes.get("picture");
    }
}
