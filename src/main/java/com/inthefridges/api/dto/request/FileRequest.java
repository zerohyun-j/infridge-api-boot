package com.inthefridges.api.dto.request;

import jakarta.validation.constraints.PositiveOrZero;

public record FileRequest(
        @PositiveOrZero(message = "아이디는 음수일 수 없습니다.")
        Long id,
        @PositiveOrZero(message = "냉장고 아이디는 음수일 수 없습니다.")
        Long fridgeId,
        @PositiveOrZero(message = "식품 아이디는 음수일 수 없습니다.")
        Long itemId
) {
}
