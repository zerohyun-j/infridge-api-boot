package com.inthefridges.api.service;

import com.inthefridges.api.entity.ProfileImage;

public interface ProfileImageService {
    ProfileImage get(Long memberId);
}
