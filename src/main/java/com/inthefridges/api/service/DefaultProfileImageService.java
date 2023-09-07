package com.inthefridges.api.service;

import com.inthefridges.api.entity.ProfileImage;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultProfileImageService implements ProfileImageService {
    private final ProfileImageRepository repository;
    @Override
    public ProfileImage getAndUpdate(ProfileImage profileImage) {
        Long memberId = profileImage.getMemberId();
        ProfileImage fetchProfileImage = getByMemberId(memberId);

        if(isPathChanged(fetchProfileImage.getPath(),profileImage.getPath())){
            fetchProfileImage.setPath(profileImage.getPath());
            ProfileImage updateProfileImage = update(fetchProfileImage);
            return updateProfileImage;
        }

        return fetchProfileImage;
    }

    @Transactional
    @Override
    public ProfileImage update(ProfileImage profileImage) {
        repository.delete(profileImage.getMemberId());
        repository.create(profileImage);
        return getByMemberId(profileImage.getMemberId());
    }

    @Override
    public ProfileImage getByMemberId(Long memberId) {
        return repository.findByMemberId(memberId)
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND));
    }

    /**
     * 현재 프로필 사진 경로와 DB 프로필 사진 경로가 같은 지 확인
     */
    private Boolean isPathChanged(String currentPath, String inputPath){
        return !inputPath.equals(currentPath);
    }


}
