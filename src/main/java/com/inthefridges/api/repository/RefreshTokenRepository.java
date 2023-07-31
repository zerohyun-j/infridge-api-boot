package com.inthefridges.api.repository;

import com.inthefridges.api.entity.RefreshToken;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface RefreshTokenRepository {
    Optional<RefreshToken> findById(String refreshToken);
    Optional<RefreshToken> findByMemberId(Long memberId);
    int create(RefreshToken refreshToken);
    void delete(String RefreshToken);
}
