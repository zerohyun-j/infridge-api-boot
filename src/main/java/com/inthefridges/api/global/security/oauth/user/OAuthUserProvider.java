package com.inthefridges.api.global.security.oauth.user;

import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OAuthUserProvider {
    GOOGLE("google") {
        @Override
        public OAuthUserInfo toUserInfo(OAuth2User oAuth2User) {
            return new GoogleUser(oAuth2User.getAttributes());
        }
    };

    private final String name;

    public static OAuthUserInfo getOAuthUserInfo(String socialName, OAuth2User oAuth2User) {
        OAuthUserProvider provider = findBySocialName(socialName);
        return provider.toUserInfo(oAuth2User);
    }

    /**
     *  providerName으로 해당 속성의 OAuthUserProvider를 반환한다
     */
    private static OAuthUserProvider findBySocialName(String socialName) {
        return Arrays.stream(OAuthUserProvider.values())
                .filter(provider -> provider.getName().equals(socialName))
                .findAny()
                .orElseThrow(
                        () -> new ServiceException(ExceptionCode.NOT_SUPPORTED_SOCIAL)
                );
    }

    /**
     *  OAuthUserInfo 객체를 반환하는 추상 메서드
     *  enum OAuthUserProvider의 각 속성은 이를 @Override하여 OAuthUserInfo를 return한다
     */
    public abstract OAuthUserInfo toUserInfo(OAuth2User oAuth2User);
}
