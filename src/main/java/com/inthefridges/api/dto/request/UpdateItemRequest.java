package com.inthefridges.api.dto.request;

import jakarta.validation.constraints.*;

import java.util.Date;

public record UpdateItemRequest(

        @NotNull(message = "아이디는 필수 값입니다.")
        Long id,

        @NotBlank(message = "식품 이름은 빈칸으로 둘 수 없습니다.")
        @Size(max = 50, message = "식품 이름은 한 글자 이상, 50자 이내로 작성해야 합니다.")
        String name,

        @NotNull(message = "수량은 빈칸으로 둘 수 없습니다.")
        @Positive(message = "수량은 1보다 작을 수 없습니다.")
        @Max(value = 100000)
        int quantity,

        @NotNull(message = "유효기간은 빈칸으로 둘 수 없습니다.")
        @FutureOrPresent(message = "이전 날짜로 유효기간을 등록할 수 없습니다.")
        Date expirationAt,

        @NotNull(message = "카테고리 아이디는 빈칸으로 둘 수 없습니다.")
        @PositiveOrZero(message = "카테고리 아이디는 음수일 수 없습니다.")
        int categoryId,

        @NotNull(message = "저장 타입 아이디는 빈칸으로 둘 수 없습니다.")
        @PositiveOrZero(message = "저장 타입 아이디는 음수일 수 없습니다.")
        int storageTypeId
) {
}
