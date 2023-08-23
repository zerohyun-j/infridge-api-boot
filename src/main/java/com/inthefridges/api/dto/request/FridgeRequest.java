package com.inthefridges.api.dto.request;

public record FridgeRequest(
        String name,
        int[] category,
        String thumbnail
 ) {
}
