package com.inthefridges.api.controller.fridge;

import com.inthefridges.api.dto.request.FridgeRequest;
import com.inthefridges.api.dto.response.FridgeResponse;
import com.inthefridges.api.entity.Fridge;
import com.inthefridges.api.global.security.jwt.model.JwtAuthentication;
import com.inthefridges.api.service.FridgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/fridges")
@RequiredArgsConstructor
public class FridgeController {

    private final FridgeService service;

    @GetMapping
    public ResponseEntity<List<FridgeResponse>> getList(@AuthenticationPrincipal JwtAuthentication member){
        List<FridgeResponse> list = service.getList(member.id());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FridgeResponse> get(@PathVariable Long id){
        FridgeResponse fridgeResponse = service.get(id);
        return ResponseEntity.ok(fridgeResponse);
    }

    @PostMapping
    public ResponseEntity<FridgeResponse> create(@Valid @RequestBody FridgeRequest fridgeRequest, @AuthenticationPrincipal JwtAuthentication member){
        FridgeResponse fridgeResponse = service.create(member.id(), fridgeRequest);
        return ResponseEntity.ok(fridgeResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FridgeResponse> update(@PathVariable Long id, @RequestBody Fridge fridge, @AuthenticationPrincipal JwtAuthentication member){
        FridgeResponse fridgeResponse = service.update(id, member.id(), fridge);
        return ResponseEntity.ok(fridgeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal JwtAuthentication member){
        service.delete(id, member.id());
        return ResponseEntity.noContent().build();
    }

}
