package com.inthefridges.api.service;

import com.inthefridges.api.dto.request.FridgeRequest;
import com.inthefridges.api.dto.response.FridgeResponse;
import com.inthefridges.api.entity.Fridge;

import java.util.List;

public interface FridgeService {
    List<FridgeResponse> getList(Long memberId);
    FridgeResponse get(Long memberId, Long Id);
    FridgeResponse create(Fridge fridge, Long fileId);
    FridgeResponse update(Long id, Fridge fridge, Long fileId);
    void delete(Long id, Long memberId);
}
