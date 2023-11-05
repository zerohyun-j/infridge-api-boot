package com.inthefridges.api.mapper;

import com.inthefridges.api.dto.request.CreateFridgeRequest;
import com.inthefridges.api.dto.request.UpdateFridgeRequest;
import com.inthefridges.api.dto.response.FridgeResponse;
import com.inthefridges.api.entity.Fridge;

import java.util.Date;

public class FridgeMapper {
    public static Fridge toFridge(CreateFridgeRequest createFridgeRequest, Long memberId) {
        return Fridge.builder()
                .name(createFridgeRequest.name())
                .memberId(memberId)
                .build();
    }

    public static Fridge toFridge(UpdateFridgeRequest updateFridgeRequest, Long memberId) {
        return Fridge.builder()
                .id(updateFridgeRequest.id())
                .name(updateFridgeRequest.name())
                .memberId(memberId)
                .build();
    }

    public static FridgeResponse toFridgeResponse(Fridge fridge, Date expirationAt, String imagePath) {
        return new FridgeResponse(fridge.getId(), fridge.getName(), expirationAt, imagePath);
    }
}
