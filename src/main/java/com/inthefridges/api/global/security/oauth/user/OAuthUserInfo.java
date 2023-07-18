package com.inthefridges.api.global.security.oauth.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class OAuthUserInfo {
    protected Map<String, Object> attributes;

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getProvider();

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getProfileImageUrl();
}
