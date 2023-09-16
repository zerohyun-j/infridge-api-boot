package com.inthefridges.api.service;

import com.inthefridges.api.entity.RefreshToken;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultRefreshTokenService implements RefreshTokenService{
    private final RefreshTokenRepository repository;

    @Override
    public void createOrUpdate(RefreshToken refreshToken) {
        repository.findByMemberId(refreshToken.getMemberId())
                .ifPresentOrElse(
                        existingToken -> repository.update(refreshToken),
                        () -> repository.save(refreshToken)
                );
    }

    @Override
    public RefreshToken get(String refreshToken) {
        return repository.findById(refreshToken)
                .orElseThrow(()->new ServiceException(ExceptionCode.NOT_FOUND_REFRESH_TOKEN));
    }
}
