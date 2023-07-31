package com.inthefridges.api.service;

import com.inthefridges.api.entity.ProfileImage;

public interface ProfileImageService {
    ProfileImage getAndUpdate(ProfileImage profileImage);
    ProfileImage update(ProfileImage profileImage);
    ProfileImage getByMemberId(Long MemberId);

}
