package com.inthefridges.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    private Long id;
    private Long memberId;
    private String token;
    private String role;
    private Date expiryDate;
    private Date createAt;
    private Date deletedAt;
    private Date updatedAt;
}
