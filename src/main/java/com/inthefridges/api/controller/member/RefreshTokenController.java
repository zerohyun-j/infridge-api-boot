package com.inthefridges.api.controller.member;

import com.inthefridges.api.global.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/refresh")
public class RefreshTokenController {
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<String> refreshAccessToken(
            @CookieValue("refreshToken") String refreshToken
    ) {
        String accessToken = jwtService.getAccessTokensByRefreshToken(refreshToken);
        return ResponseEntity.ok().body(accessToken);
    }

    @DeleteMapping
    public ResponseEntity<Void> expireRefreshToken(@CookieValue("refreshToken") String refreshToken){
        jwtService.deleteRefreshToken(refreshToken);
        return ResponseEntity.noContent().build();
    }

}
