package com.inthefridges.api.service;

import com.inthefridges.api.entity.Member;
import com.inthefridges.api.global.security.oauth.user.OAuthUserInfo;

public interface MemberService {
    Member getOrCreate(OAuthUserInfo oAuthUserInfo);
}
