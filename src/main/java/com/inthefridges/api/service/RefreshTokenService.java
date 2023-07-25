package com.inthefridges.api.service;

import com.inthefridges.api.entity.RefreshToken;

public interface RefreshTokenService {
    void createOrUpdate(RefreshToken refreshToken);
}
