package com.inthefridges.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UpdateFridgeRequest(
        @NotNull(message = "아이디는 필수 값입니다.")
        Long id,
        @NotBlank(message = "이름은 빈칸으로 둘 수 없습니다.")
        @Size(max = 20, message = "이름은 한 글자 이상, 20자 이내로 작성해야 합니다.")
        String name,
        @PositiveOrZero(message = "파일 아이디는 음수일 수 없습니다.")
        Long fileId
) {
}
