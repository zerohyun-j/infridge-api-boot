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
public class FridgeInvite {
    private Long id;
    private Long fromMemberId;
    private Long toMemberId;
    private Long fridgeId;
    private Boolean accept;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
