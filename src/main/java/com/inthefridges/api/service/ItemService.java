package com.inthefridges.api.service;

import com.inthefridges.api.dto.request.ItemRequest;
import com.inthefridges.api.dto.response.ItemResponse;

import java.util.List;

public interface ItemService {
    ItemResponse create(ItemRequest itemRequest, Long memberId, Long fridgeId);
    ItemResponse get(Long memberId, Long fridgeId, Long id);
    List<ItemResponse> getList(Long memberId, Long fridgeId, int storageId);
    ItemResponse update(ItemRequest itemRequest, Long id, Long memberId, Long fridgeId);
    void delete(Long memberId, Long fridgeId, Long id);
}
