package com.inthefridges.api.global.security.oauth.service;

import com.inthefridges.api.entity.CustomUserDetails;
import com.inthefridges.api.entity.Member;
import com.inthefridges.api.entity.ProfileImage;
import com.inthefridges.api.global.security.oauth.user.OAuthUserInfo;
import com.inthefridges.api.global.security.oauth.user.OAuthUserProvider;
import com.inthefridges.api.service.MemberService;
import com.inthefridges.api.service.ProfileImageService;
import com.inthefridges.api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOauthUserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final ProfileImageService profileImageService;
    private final RoleService roleService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String socialName = userRequest.getClientRegistration().getRegistrationId();

        OAuthUserInfo oAuthUserInfo = OAuthUserProvider.getOAuthUserInfo(socialName, oAuth2User);

        // 회원 조회 및 저장
        Member member = memberService.getOrCreate(oAuthUserInfo);

        // 조회된 멤버의 아이디로 프로필 이미지, 권한 조회
        ProfileImage profileImage = profileImageService.get(member.getId());
        List<GrantedAuthority> authorities = roleService.getList(member.getId());

        return new CustomUserDetails(member, authorities, oAuthUserInfo.getAttributes(), profileImage);
    }

}
