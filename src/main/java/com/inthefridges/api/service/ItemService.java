package com.inthefridges.api.service;

import com.inthefridges.api.dto.request.CreateItemRequest;
import com.inthefridges.api.dto.request.UpdateItemRequest;
import com.inthefridges.api.dto.response.ItemResponse;

import java.util.List;

public interface ItemService {
    ItemResponse create(CreateItemRequest createItemRequest, Long memberId, Long fridgeId);
    ItemResponse get(Long memberId, Long fridgeId, Long id);
    List<ItemResponse> getList(Long memberId, Long fridgeId, int storageId);
    ItemResponse update(UpdateItemRequest updateItemRequest, Long id, Long memberId, Long fridgeId);
    void delete(Long memberId, Long fridgeId, Long id);
}
