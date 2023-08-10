package com.inthefridges.api.service;

import com.inthefridges.api.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken get(String RefreshToken);
    void createOrUpdate(RefreshToken refreshToken);
}
