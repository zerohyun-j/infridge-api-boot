package com.inthefridges.api.service;

import com.inthefridges.api.entity.ProfileImage;
import org.springframework.stereotype.Service;

@Service
public class DefaultProfileImageService implements ProfileImageService {
    @Override
    public ProfileImage get(Long memberId) {
        return null;
    }
}
