package com.inthefridges.api.service;

import com.inthefridges.api.dto.request.FileRequest;
import com.inthefridges.api.dto.response.FileResponse;
import com.inthefridges.api.entity.InFridgeFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileResponse get(Long memberId, FileRequest fileRequest);
    InFridgeFile getProfileImageAndUpdate(InFridgeFile file);
    FileResponse create(Long memberId, MultipartFile file);
    FileResponse update(Long memberId, FileRequest fileRequest);
    void delete(Long memberId, FileRequest fileRequest);

}
