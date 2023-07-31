package com.inthefridges.api.service;

import com.inthefridges.api.entity.RefreshToken;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultRefreshTokenService implements RefreshTokenService{
    private final RefreshTokenRepository repository;

    @Override
    public void create(RefreshToken refreshToken) {
        repository.findByMemberId(refreshToken.getMemberId())
                .ifPresent(existingToken -> repository.delete(existingToken.getToken()));
        repository.create(refreshToken);
    }

    @Override
    public RefreshToken get(String refreshToken) {
        return repository.findById(refreshToken)
                .orElseThrow(()->new ServiceException(ExceptionCode.NOT_FOUND_REFRESH_TOKEN));
    }
}
