package com.inthefridges.api.global.security.jwt;

import com.inthefridges.api.entity.CustomUserDetails;
import com.inthefridges.api.entity.Member;
import com.inthefridges.api.entity.RefreshToken;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.global.security.jwt.model.JwtAuthentication;
import com.inthefridges.api.global.security.jwt.model.JwtAuthenticationToken;
import com.inthefridges.api.global.security.jwt.model.Tokens;
import com.inthefridges.api.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private final static String BEARER_TYPE = "Bearer";
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public Tokens createTokens(CustomUserDetails userInfo){
        Member member = userInfo.getMember();

        Long id = member.getId();
        String roles = userInfo.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

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

    public void deleteRefreshToken(String refreshToken) {
        checkRefreshToken(refreshToken);
        RefreshToken token = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_REFRESH_TOKEN));
        refreshTokenRepository.delete(token.getToken());
    }

    public String getAccessTokensByRefreshToken(String refreshToken) {
        // 토큰 검증
        checkRefreshToken(refreshToken);
        jwtProvider.validateToken(refreshToken);

        RefreshToken token = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(()->new ServiceException(ExceptionCode.NOT_FOUND_REFRESH_TOKEN));
        return jwtProvider.createAccessToken(token.getMemberId(), token.getRole());
    }

    private void checkRefreshToken(String refreshToken) {
        if (Objects.isNull(refreshToken) || refreshToken.isBlank()) {
            new ServiceException(ExceptionCode.NOT_FOUND_COOKIE);
        }
    }
}
