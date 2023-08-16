package com.inthefridges.api.global.security.jwt.filter;

import com.inthefridges.api.global.security.jwt.JwtService;
import com.inthefridges.api.global.security.jwt.model.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtService.getTokenFromRequest(request);

        if(accessToken != null) {
            JwtAuthenticationToken authenticationToken = jwtService.getAuthenticationByAccessToken(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
       }

        filterChain.doFilter(request, response);
    }

}
