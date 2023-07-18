package com.inthefridges.api.service;

import com.inthefridges.api.entity.Member;
import com.inthefridges.api.global.security.oauth.user.OAuthUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultMemberService implements MemberService{
    @Override
    public Member getOrCreate(OAuthUserInfo oAuthUserInfo) {
        log.info("oauthUserInfo {}", oAuthUserInfo);
        return null;
    }
}
