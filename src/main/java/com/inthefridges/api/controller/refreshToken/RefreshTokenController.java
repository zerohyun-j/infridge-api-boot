package com.inthefridges.api.controller.refreshToken;

import com.inthefridges.api.global.security.jwt.JwtService;
import com.inthefridges.api.global.security.jwt.model.Tokens;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
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
        Tokens tokens = jwtService.getAccessTokensByRefreshToken(refreshToken);
        ResponseCookie responseCookie = setRefreshTokenInCookie(tokens.refreshToken());
        return ResponseEntity.ok()
                .header("Set-Cookie", responseCookie.toString())
                .body(tokens.accessToken());
    }

    @DeleteMapping
    public ResponseEntity<Void> expireRefreshToken(@CookieValue("refreshToken") String refreshToken){
        jwtService.deleteRefreshToken(refreshToken);
        return ResponseEntity.noContent()
                .header("Set-Cookie", removeCookie().toString()).build();
    }

    private ResponseCookie setRefreshTokenInCookie(String refreshToken){
        return ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(jwtService.getRefreshExpirationMillis())
                .build();
    }

    private ResponseCookie removeCookie() {
        return ResponseCookie.from("refreshToken", null)
                .maxAge(0)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .build();
    }

}
