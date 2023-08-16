package com.inthefridges.api.global.security.handler;

import com.inthefridges.api.entity.CustomUserDetails;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.inthefridges.api.global.security.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final HttpCookieOAuthAuthorizationRequestRepository authorizationRepository;
    @Value("${oauth2.authorized-redirect-uris}")
    private final List<String> authorizedRedirectUris;

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
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get()))
            throw new ServiceException(ExceptionCode.INVALID_URI);

        return UriComponentsBuilder.fromUriString(redirectUri.orElse(getDefaultTargetUrl()))
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

    private boolean isAuthorizedRedirectUri(String uri){
        URI clientRedirectUri = URI.create(uri);
        return authorizedRedirectUris
                .stream()
                .anyMatch(authorizedRedirectUris -> {
                    URI authorizedURI = URI.create(authorizedRedirectUris);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                        && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}
