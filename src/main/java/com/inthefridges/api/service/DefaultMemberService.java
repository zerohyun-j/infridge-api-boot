package com.inthefridges.api.service;

import com.inthefridges.api.dto.response.MemberResponse;
import com.inthefridges.api.entity.*;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.global.security.oauth.user.OAuthUserInfo;
import com.inthefridges.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMemberService implements MemberService{
    private final MemberRepository repository;
    private final SocialRepository socialRepository;
    private final FileRepository fileRepository;
    private final RoleRepository roleRepository;
    private final MemberRoleRepository memberRoleRepository;
    private static final String DEFAULT_ROLE = "MEMBER";

    /**
     * 기존 가입된 회원이라면 회원을 반환, 가입 회원이 아닐 경우 회원가입*/
    @Override
    public Member getOrCreate(OAuthUserInfo oAuthUserInfo) {
        Member member = repository.findBySocialId(oAuthUserInfo.getId())
                        .orElseGet(() -> create(oAuthUserInfo));
        return member;
    }

    @Transactional
    @Override
    public Member create(OAuthUserInfo oAuthUserInfo) {

        Social social = socialRepository.findBySocialName(oAuthUserInfo.getProvider())
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_SUPPORTED_SOCIAL));

        Member member = Member.builder()
                        .email(oAuthUserInfo.getEmail())
                        .socialId(oAuthUserInfo.getId())
                        .socialTypeId(social.getId())
                        .username(oAuthUserInfo.getEmail().substring(0,oAuthUserInfo.getEmail().indexOf("@")))
                        .build();
        repository.save(member);

        String path = oAuthUserInfo.getProfileImageUrl();
        InFridgeFile profileImage = InFridgeFile.builder()
                                    .path(path)
                                    .originName(path)
                                    .memberId(member.getId())
                                    .build();
        fileRepository.save(profileImage);

        Role role = roleRepository.findByRoleName(DEFAULT_ROLE.toLowerCase())
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND));
        memberRoleRepository.save(
                MemberRole.builder()
                        .memberId(member.getId())
                        .roleId(role.getId())
                        .build()
        );

        return member;
    }

    @Override
    public MemberResponse getProfile(Long id) {
        Member member = repository.findByMemberId(id)
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_MEMBER));
        InFridgeFile profileImage = fileRepository.findByMemberId(id)
                                    .orElse(new InFridgeFile());
        List<String> roles = memberRoleRepository.findByMemberId(member.getId());
        return new MemberResponse(member.getId(), member.getUsername(), profileImage.getPath(), roles);
    }
}
