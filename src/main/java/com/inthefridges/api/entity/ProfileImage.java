package com.inthefridges.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProfileImage {
    private Long id;
    private Long memberId;
    private String path;
    private Date createAt;
    private Date deleteAt;

    @Builder
    public ProfileImage(Long memberId, String path) {
        this.memberId = memberId;
        this.path = path;
    }
}
