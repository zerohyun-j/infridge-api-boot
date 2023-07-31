package com.inthefridges.api.global.security.handler;

import com.inthefridges.api.entity.CustomUserDetails;
import com.inthefridges.api.global.security.jwt.JwtService;
import com.inthefridges.api.global.security.jwt.model.Tokens;
import com.inthefridges.api.global.security.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository;
import com.inthefridges.api.global.utils.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.inthefridges.api.global.security.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final HttpCookieOAuthAuthorizationRequestRepository authorizationRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
        Tokens tokens = jwtService.createTokens(customUserDetails);
        String redirectUri = getRedirectUri(request, tokens.accessToken());
        setRefreshTokenInCookie(response, tokens.refreshToken());

        // HttpCookieOAuthAuthorizationRequestRepository 에서 생성된 쿠키 제거
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }

    private String getRedirectUri(HttpServletRequest request, String accessToken) {
        String redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(getDefaultTargetUrl());

        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("accessToken", accessToken)
                .build().toUriString();
    }

    private void setRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie token = ResponseCookie.from("refreshToken", refreshToken)
                .path(getDefaultTargetUrl())
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(jwtService.getRefreshExpirationMillis())
                .build();

        response.addHeader("Set-Cookie", token.toString());
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRepository.removeAuthorizationRequestCookies(request, response);
    }
}
