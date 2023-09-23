package com.inthefridges.api.service;

import com.inthefridges.api.dto.response.ItemResponse;
import com.inthefridges.api.entity.Item;

import java.util.List;

public interface ItemService {
    ItemResponse create(Item item);
    ItemResponse get(Long memberId, Long fridgeId, Long id);
    List<ItemResponse> getList(Long memberId, Long fridgeId, int storageId);
    ItemResponse update(Item item);
    void delete(Long memberId, Long fridgeId, Long id);
}
