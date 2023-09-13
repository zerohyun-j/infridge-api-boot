package com.inthefridges.api.service;

import com.inthefridges.api.dto.request.FridgeRequest;
import com.inthefridges.api.dto.response.FridgeResponse;
import com.inthefridges.api.entity.Fridge;

import java.util.List;

public interface FridgeService {
    List<FridgeResponse> getList(Long memberId);
    FridgeResponse get(Long memberId, Long Id);
    FridgeResponse create(Long memberId, FridgeRequest fridgeRequest);
    FridgeResponse update(Long id, Long memberId, Fridge fridge);
    void delete(Long id, Long memberId);
}
