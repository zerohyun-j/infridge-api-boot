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
public class File {
    private Long id;
    private Long postId;
    private int categoryId;
    private String originName;
    private String path;
    private Date createdAt;
    private Date deletedAt;
}
