package com.inthefridges.api.dto.request;

public record FileRequest(
        Long id,
        Long fridgeId,
        Long itemId
) {
}
