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
public class Item {
    private Long id;
    private String name;
    private Long memberId;
    private int quantity;
    private Date expirationAt;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Long fridgeId;
    private Long categoryId;
}
