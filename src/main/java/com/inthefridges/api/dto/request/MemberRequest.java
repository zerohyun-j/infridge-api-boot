package com.inthefridges.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberRequest(
        @NotBlank(message = "닉네임은 빈칸으로 둘 수 없습니다.")
        @Size(max = 50, message = "닉네임은은 한 글자 이상, 99자 이내로 작성해야 합니다.")
        String username
) {
}
