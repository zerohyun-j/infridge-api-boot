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
public class InFridgeFile {
    private Long id;
    private Long fridgeId;
    private Long itemId;
    private Long memberId;
    private String originName;
    private String path;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
