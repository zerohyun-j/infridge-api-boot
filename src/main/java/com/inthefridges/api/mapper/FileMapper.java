package com.inthefridges.api.mapper;

import com.inthefridges.api.dto.request.FileRequest;
import com.inthefridges.api.dto.response.FileResponse;
import com.inthefridges.api.entity.InFridgeFile;

public class FileMapper {
    public static InFridgeFile toFile(FileRequest fileRequest, Long memberId) {
        return InFridgeFile.builder()
                .id(fileRequest.id())
                .memberId(memberId)
                .fridgeId(fileRequest.fridgeId())
                .itemId(fileRequest.itemId())
                .build();
    }

    public static FileResponse toFileResponse(InFridgeFile file, String fullPath) {
        return new FileResponse(file.getId(), fullPath, file.getOriginName());
    }
}
