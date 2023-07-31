package com.inthefridges.api.global.security.jwt.model;

import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;

import java.util.Objects;

public record JwtAuthentication(
        Long id,
        String accessToken
) {
    public JwtAuthentication{
        validateId(id);
        validateToken(accessToken);
    }

    private void validateToken(String accessToken) {
        if(Objects.isNull(accessToken) || accessToken.isBlank())
            throw new ServiceException(ExceptionCode.INVALID_TOKEN);
    }

    private void validateId(Long id) {
        if(Objects.isNull(id) || id <= 0L)
            throw new ServiceException(ExceptionCode.INVALID_TOKEN);
    }


}
