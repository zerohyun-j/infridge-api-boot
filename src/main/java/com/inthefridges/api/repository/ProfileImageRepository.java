package com.inthefridges.api.repository;

import com.inthefridges.api.entity.ProfileImage;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ProfileImageRepository {
    Optional<ProfileImage> findByMemberId(Long memberId);
    int create(ProfileImage profileImage);
    int delete(Long memberId);
}
