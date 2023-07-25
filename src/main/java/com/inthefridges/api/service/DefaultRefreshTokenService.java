package com.inthefridges.api.service;

import com.inthefridges.api.entity.RefreshToken;
import com.inthefridges.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultRefreshTokenService implements RefreshTokenService{
    private final RefreshTokenRepository repository;

    @Override
    public void createOrUpdate(RefreshToken refreshToken) {
        int result = repository.create(refreshToken);
        // TODO : 예외처리 및 트랜잭션 처리
    }
}
