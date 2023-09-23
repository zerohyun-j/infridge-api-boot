package com.inthefridges.api.dto.response;

import com.inthefridges.api.entity.Category;

import java.util.Date;

public record ItemResponse(
        Long id,
        String name,
        int quantity,
        Date expirationAt,
        int storageTypeId,
        Category category
) {
}
