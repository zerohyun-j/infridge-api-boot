package com.inthefridges.api.service;

import com.inthefridges.api.entity.Member;
import com.inthefridges.api.global.security.oauth.user.OAuthUserInfo;
import org.springframework.stereotype.Service;

@Service
public class DefaultMemberService implements MemberService{
    @Override
    public Member getOrCreate(OAuthUserInfo oAuthUserInfo) {
        return null;
    }
}
