package com.inthefridges.api.global.security.jwt.model;

public record Tokens(
        String accessToken,
        String refreshToken
) {

}
