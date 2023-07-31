package com.inthefridges.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
    private Long id;
    private String email;
    private String username;
    private String socialId;
    private int socialTypeId;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
