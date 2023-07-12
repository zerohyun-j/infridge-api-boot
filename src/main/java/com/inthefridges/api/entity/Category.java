package com.inthefridges.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;


@Getter
@AllArgsConstructor
public class Category {
    private Long id;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
