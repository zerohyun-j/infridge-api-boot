package com.inthefridges.api.global.security.jwt;

import com.inthefridges.api.entity.RefreshToken;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.service.RefreshTokenService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {

    private String secretKey;
    // 900,000ms = 15분
    private Long accessExpirationMillis;
    private Long refreshExpirationMillis;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public JwtProvider(@Value("") String secretKey, @Value("") Long accessExpirationMillis, @Value("") Long refreshExpirationMillis){
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

    public String createRefreshToken(Long userId, String role){
        String token = createToken(userId, role, refreshExpirationMillis);
        RefreshToken refreshToken = new RefreshToken(userId, token);
        refreshTokenService.createOrUpdate(refreshToken);
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
        }catch(ExpiredJwtException e){
            throw new ServiceException(ExceptionCode.EXPIRED_TOKEN);
        }catch(JwtException | IllegalArgumentException e){
            throw new ServiceException(ExceptionCode.INVALID_TOKEN);
        }
    }

    public Long getRefreshExpirationMillis() {
        return refreshExpirationMillis;
    }
}
