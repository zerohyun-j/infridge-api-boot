package com.inthefridges.api.service;

import com.inthefridges.api.dto.request.MemberRequest;
import com.inthefridges.api.dto.response.MemberResponse;
import com.inthefridges.api.entity.Member;
import com.inthefridges.api.global.security.oauth.user.OAuthUserInfo;

public interface MemberService {
    Member getOrCreate(OAuthUserInfo oAuthUserInfo);
    Member create(OAuthUserInfo oAuthUserInfo);
    MemberResponse getProfile(Long id);
    MemberResponse update(Long id, MemberRequest memberRequest);
    void delete(Long memberId);
}
