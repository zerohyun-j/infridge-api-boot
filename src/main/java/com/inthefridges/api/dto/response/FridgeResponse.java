package com.inthefridges.api.dto.response;

import java.util.Date;

public record FridgeResponse(
        Long id,
        String name,
        String img,
        Date deadLine
) {
}
