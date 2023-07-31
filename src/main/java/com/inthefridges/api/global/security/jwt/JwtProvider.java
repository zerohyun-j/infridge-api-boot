package com.inthefridges.api.global.security.jwt;

import com.inthefridges.api.entity.RefreshToken;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.service.RefreshTokenService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {

    private final String secretKey;
    private final Long accessExpirationMillis;
    private final Long refreshExpirationMillis;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public JwtProvider(@Value("${jwt.secret-key}") String secretKey, @Value("${jwt.expiry-millis.access-token}") Long accessExpirationMillis, @Value("${jwt.expiry-millis.refresh-token}") Long refreshExpirationMillis){
        this.secretKey = secretKey;
        this.accessExpirationMillis = accessExpirationMillis;
        this.refreshExpirationMillis = refreshExpirationMillis;
    }

    public String createToken(Long userId, String role, Long TokenValidTime){

        // 토큰 만료 기간
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + TokenValidTime);

        Map<String, Object> claims = Map.of("userId", userId, "role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createAccessToken(Long userId, String role){
        return createToken(userId, role, accessExpirationMillis);
    }

    public String createRefreshToken(Long memberId, String role){
        String token = createToken(memberId, role, refreshExpirationMillis);
        RefreshToken refreshToken = RefreshToken.builder()
                                    .token(token)
                                    .memberId(memberId)
                                    .role(role)
                                    .build();
        refreshTokenService.create(refreshToken);
        return token;
    }

    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        }catch(ExpiredJwtException e){ // TODO : 응답으로 에러 반환
            throw new ServiceException(ExceptionCode.EXPIRED_TOKEN);
        }catch(JwtException | IllegalArgumentException e){
            throw new ServiceException(ExceptionCode.INVALID_TOKEN);
        }
    }

    public Long getRefreshExpirationMillis() {
        return refreshExpirationMillis;
    }
}
