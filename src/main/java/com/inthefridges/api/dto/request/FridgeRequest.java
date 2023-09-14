package com.inthefridges.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record FridgeRequest(
        @NotBlank(message = "제목은 빈칸으로 둘 수 없습니다.")
        @Size(max = 20, message = "제목은 한 글자 이상, 20자 이내로 작성해야 합니다.")
        String name,
        @PositiveOrZero(message = "아이디는 음수일 수 없습니다.")
        Long fileId
 ) {
}
