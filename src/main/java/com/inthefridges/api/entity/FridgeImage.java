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
public class FridgeImage {
    Long id;
    Long fridgeId;
    String path;
    Date createdAt;
    Date deletedAt;
}
