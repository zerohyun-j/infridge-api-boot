package com.inthefridges.api.controller.member;

import com.inthefridges.api.dto.request.MemberRequest;
import com.inthefridges.api.dto.response.MemberResponse;
import com.inthefridges.api.global.security.jwt.model.JwtAuthentication;
import com.inthefridges.api.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members/")
public class MemberController {
    private final MemberService service;

    @GetMapping("me")
    public ResponseEntity<MemberResponse> get(@AuthenticationPrincipal JwtAuthentication jwtAuthentication){
        MemberResponse memberResponse = service.getProfile(jwtAuthentication.id());
        return ResponseEntity.ok(memberResponse);
    }

    @PutMapping("me")
    public ResponseEntity<MemberResponse> update(@AuthenticationPrincipal JwtAuthentication jwtAuthentication, @RequestBody @Valid MemberRequest memberRequest){
        MemberResponse memberResponse = service.update(jwtAuthentication.id(), memberRequest);
        return ResponseEntity.ok(memberResponse);
    }

    @DeleteMapping("me")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal JwtAuthentication jwtAuthentication){
        service.delete(jwtAuthentication.id());
        return ResponseEntity.noContent().build();
    }

}
