package com.inthefridges.api.service;

import com.inthefridges.api.dto.request.CreateFridgeRequest;
import com.inthefridges.api.dto.request.UpdateFridgeRequest;
import com.inthefridges.api.dto.response.FridgeResponse;

import java.util.List;

public interface FridgeService {
    List<FridgeResponse> getList(Long memberId);
    FridgeResponse get(Long memberId, Long Id);
    FridgeResponse create(CreateFridgeRequest createFridgeRequest, Long memberId, Long fileId);
    FridgeResponse update(Long id, UpdateFridgeRequest updateFridgeRequest, Long memberId, Long fileId);
    void delete(Long id, Long memberId);
}
