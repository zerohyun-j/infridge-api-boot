package com.inthefridges.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record FridgeRequest(
        @NotBlank(message = "제목은 빈칸으로 둘 수 없습니다.")
        @Max(value = 20, message = "제목은 한 글자 이상, 20자 이내로 작성해야 합니다.")
        String name
//        @NotBlank(message = "타입은 빈칸으로 둘 수 없습니다.")
//        @PositiveOrZero(message = "타입은 0을 포함한 양수여야 합니다.")
//        Long[] type
 ) {
}
