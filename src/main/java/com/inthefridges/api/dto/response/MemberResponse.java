package com.inthefridges.api.dto.response;

import java.util.List;

public record MemberResponse(
         Long id,
         String username,
         String email,
         String profileImage,
         List<String> role
) {
}
