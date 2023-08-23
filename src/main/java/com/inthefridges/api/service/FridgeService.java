package com.inthefridges.api.service;

import com.inthefridges.api.dto.response.FridgeResponse;
import com.inthefridges.api.entity.Fridge;

import java.util.List;

public interface FridgeService {
    List<FridgeResponse> getList(Long memberId);
    FridgeResponse get(Long Id);
    FridgeResponse create(Long memberId, Fridge fridge);
    void update(Long memberId, Fridge fridge);
    void delete(Long id, Long memberId);
}
