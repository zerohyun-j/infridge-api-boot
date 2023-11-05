package com.inthefridges.api.mapper;

import com.inthefridges.api.dto.request.CreateItemRequest;
import com.inthefridges.api.dto.request.UpdateItemRequest;
import com.inthefridges.api.dto.response.ItemResponse;
import com.inthefridges.api.entity.Category;
import com.inthefridges.api.entity.Item;

public class ItemMapper {
    public static Item toItem(CreateItemRequest createItemRequest, Long memberId, Long fridgeId) {
        return Item.builder()
                .name(createItemRequest.name())
                .expirationAt(createItemRequest.expirationAt())
                .quantity(createItemRequest.quantity())
                .categoryId(createItemRequest.categoryId())
                .storageId(createItemRequest.storageTypeId())
                .fridgeId(fridgeId)
                .memberId(memberId)
                .build();
    }

    public static Item toItem(UpdateItemRequest updateItemRequest, Long memberId, Long fridgeId) {
        return Item.builder()
                .id(updateItemRequest.id())
                .name(updateItemRequest.name())
                .expirationAt(updateItemRequest.expirationAt())
                .quantity(updateItemRequest.quantity())
                .categoryId(updateItemRequest.categoryId())
                .storageId(updateItemRequest.storageTypeId())
                .fridgeId(fridgeId)
                .memberId(memberId)
                .build();
    }

    public static ItemResponse toItemResponse(Item item, Category category) {
        return new ItemResponse(item.getId(),
                item.getName(),
                item.getQuantity(),
                item.getExpirationAt(),
                item.getStorageId(),
                category);
    }
}
