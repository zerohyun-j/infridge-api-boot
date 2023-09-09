package com.inthefridges.api.dto.response;

public record FileResponse(
        Long id,
        String path,
        String originName
) {
}
