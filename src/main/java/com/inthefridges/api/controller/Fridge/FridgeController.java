package com.inthefridges.api.controller.Fridge;

import com.inthefridges.api.dto.request.FridgeRequest;
import com.inthefridges.api.dto.response.FridgeResponse;
import com.inthefridges.api.entity.CustomUserDetails;
import com.inthefridges.api.entity.Fridge;
import com.inthefridges.api.service.FridgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fridge")
@RequiredArgsConstructor
public class FridgeController {

    private final FridgeService service;

    @GetMapping
    public ResponseEntity<List<FridgeResponse>> getList(@AuthenticationPrincipal CustomUserDetails member){
        List<FridgeResponse> list = service.getList(member.getMember().getId());
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<FridgeResponse> get(@PathVariable Long id){
        FridgeResponse fridgeResponse = service.get(id);
        return ResponseEntity.ok(fridgeResponse);
    }

    @PostMapping
    public ResponseEntity<FridgeResponse> create(@RequestBody FridgeRequest fridge, @AuthenticationPrincipal CustomUserDetails member){
        FridgeResponse fridgeResponse = service.create(member.getMember().getId(), Fridge.builder().name(fridge.name()).build());
        return ResponseEntity.ok(fridgeResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<FridgeResponse> update(@PathVariable Long id, @RequestBody Fridge fridge, @AuthenticationPrincipal CustomUserDetails member){
        return null;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails member){
        service.delete(id, member.getMember().getId());
        return ResponseEntity.noContent().build();
    }

}
