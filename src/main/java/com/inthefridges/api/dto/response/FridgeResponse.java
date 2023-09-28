package com.inthefridges.api.dto.response;

import com.inthefridges.api.dto.request.FileRequest;

import java.util.Date;

public record FridgeResponse(
        Long id,
        String name,
        Date earliestExpiryAt,
        String filePath
) {
}
