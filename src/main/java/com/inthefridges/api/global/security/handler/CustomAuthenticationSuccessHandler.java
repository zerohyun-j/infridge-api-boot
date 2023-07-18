package com.inthefridges.api.global.security.handler;

import com.inthefridges.api.global.utils.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String redirectUri = getRedirectUri(request);
        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }

    private String getRedirectUri(HttpServletRequest request) {
        String redirectUri = CookieUtil.getCookie(request, "")
                .map(Cookie::getValue)
                .orElse(getDefaultTargetUrl());

        return UriComponentsBuilder.fromUriString(redirectUri).build().toUriString();
    }
}
