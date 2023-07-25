package com.inthefridges.api.global.security.jwt;

import com.inthefridges.api.entity.CustomUserDetails;
import com.inthefridges.api.entity.Member;
import com.inthefridges.api.global.security.jwt.model.JwtAuthentication;
import com.inthefridges.api.global.security.jwt.model.JwtAuthenticationToken;
import com.inthefridges.api.global.security.jwt.model.Tokens;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final static String BEARER_TYPE = "Bearer";
    private final JwtProvider jwtProvider;

    public Tokens createTokens(CustomUserDetails userInfo){
        Member member = userInfo.getMember();

        Long id = member.getId();
        String roles = userInfo.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        String accessToken = jwtProvider.createAccessToken(id, roles);
        String refreshToken = jwtProvider.createRefreshToken(id, roles);

        return new Tokens(accessToken, refreshToken);
    }

    public JwtAuthenticationToken getAuthenticationByAccessToken(String accessToke) {
        jwtProvider.validateToken(accessToke);

        Claims claims = jwtProvider.getClaims(accessToke);
        Long userId = claims.get("userId", Long.class);
        String role = claims.get("role", String.class);

        JwtAuthentication principal = new JwtAuthentication(userId, accessToke);
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        return new JwtAuthenticationToken(principal, null, authorities);
    }

    public String getTokenFromRequest(HttpServletRequest request){
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);

        return bearer == null || !bearer.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())
                ? null : bearer.substring(BEARER_TYPE.length()).trim();
    }

    public Long getRefreshExpirationMillis(){
        return jwtProvider.getRefreshExpirationMillis();
    }
}
