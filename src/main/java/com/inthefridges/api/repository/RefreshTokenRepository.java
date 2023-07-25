package com.inthefridges.api.repository;

import com.inthefridges.api.entity.RefreshToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenRepository {
    int create(RefreshToken refreshToken);
}
