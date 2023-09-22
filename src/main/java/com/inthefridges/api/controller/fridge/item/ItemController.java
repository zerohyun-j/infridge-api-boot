package com.inthefridges.api.controller.fridge.item;

import com.inthefridges.api.dto.request.ItemRequest;
import com.inthefridges.api.dto.response.ItemResponse;
import com.inthefridges.api.entity.Item;
import com.inthefridges.api.global.security.jwt.model.JwtAuthentication;
import com.inthefridges.api.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/fridges/")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @GetMapping("{fridgeId}/items")
    public ResponseEntity<List<ItemResponse>> getList(@AuthenticationPrincipal JwtAuthentication member, @PathVariable Long fridgeId, @RequestParam int storageId){
        List<ItemResponse> list = service.getList(member.id(), fridgeId, storageId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("{fridgeId}/items/{id}")
    public ResponseEntity<ItemResponse> get(@AuthenticationPrincipal JwtAuthentication member, @PathVariable Long fridgeId, @PathVariable Long id){
        ItemResponse itemResponse = service.get(member.id(), fridgeId, id);
        return ResponseEntity.ok(itemResponse);
    }

    @PutMapping("{fridgeId}/items/{id}")
    public ResponseEntity<ItemResponse> update(@AuthenticationPrincipal JwtAuthentication member, @PathVariable Long fridgeId, @PathVariable Long id, @Valid @RequestBody ItemRequest itemRequest){
        Item item = Item.builder()
                .id(id)
                .name(itemRequest.name())
                .quantity(itemRequest.quantity())
                .expirationAt(itemRequest.expirationAt())
                .categoryId(itemRequest.categoryId())
                .storageId(itemRequest.storageTypeId())
                .fridgeId(fridgeId)
                .memberId(member.id())
                .build();
        ItemResponse itemResponse = service.update(item);
        return ResponseEntity.ok(itemResponse);
    }

    @PostMapping("{fridgeId}/items")
    public ResponseEntity<ItemResponse> create(@AuthenticationPrincipal JwtAuthentication member, @Valid @RequestBody ItemRequest itemRequest, @PathVariable Long fridgeId){
        Item item = Item.builder()
                        .name(itemRequest.name())
                        .quantity(itemRequest.quantity())
                        .expirationAt(itemRequest.expirationAt())
                        .categoryId(itemRequest.categoryId())
                        .storageId(itemRequest.storageTypeId())
                        .fridgeId(fridgeId)
                        .memberId(member.id())
                        .build();
        ItemResponse itemResponse = service.create(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemResponse);
    }

    @DeleteMapping("{fridgeId}/items/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal JwtAuthentication member, @PathVariable Long fridgeId, @PathVariable Long id){
        service.delete(member.id(), fridgeId, id);
        return ResponseEntity.noContent().build();
    }

}

