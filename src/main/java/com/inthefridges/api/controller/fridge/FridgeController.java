package com.inthefridges.api.controller.fridge;

import com.inthefridges.api.dto.request.FileRequest;
import com.inthefridges.api.dto.request.FridgeRequest;
import com.inthefridges.api.dto.response.FridgeResponse;
import com.inthefridges.api.entity.Fridge;
import com.inthefridges.api.global.security.jwt.model.JwtAuthentication;
import com.inthefridges.api.service.FridgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<FridgeResponse> get(@AuthenticationPrincipal JwtAuthentication member, @PathVariable Long id){
        FridgeResponse fridgeResponse = service.get(member.id(), id);
        return ResponseEntity.ok(fridgeResponse);
    }

    @PostMapping
    public ResponseEntity<FridgeResponse> create(@AuthenticationPrincipal JwtAuthentication member, @Valid @RequestBody FridgeRequest fridgeRequest){
        Fridge fridge = Fridge.builder()
                .name(fridgeRequest.name())
                .memberId(member.id())
                .build();

        FridgeResponse fridgeResponse = service.create(fridge, fridgeRequest.fileId());
        return ResponseEntity.status(HttpStatus.CREATED).body(fridgeResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FridgeResponse> update(@AuthenticationPrincipal JwtAuthentication member, @PathVariable Long id, @RequestBody FridgeRequest fridgeRequest){
        Fridge fridge = Fridge.builder()
                .name(fridgeRequest.name())
                .memberId(member.id())
                .build();
        FridgeResponse fridgeResponse = service.update(id, fridge, fridgeRequest.fileId());
        return ResponseEntity.ok(fridgeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal JwtAuthentication member, @PathVariable Long id){
        service.delete(id, member.id());
        return ResponseEntity.noContent().build();
    }

}
