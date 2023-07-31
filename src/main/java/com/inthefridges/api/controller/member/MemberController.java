package com.inthefridges.api.controller.member;

import com.inthefridges.api.dto.response.MemberResponse;
import com.inthefridges.api.global.security.jwt.model.JwtAuthentication;
import com.inthefridges.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members/")
public class MemberController {
    private final MemberService service;

    @GetMapping("me")
    public ResponseEntity<MemberResponse> me(@AuthenticationPrincipal JwtAuthentication jwtAuthentication){
        return ResponseEntity.ok().body(service.getProfile(jwtAuthentication.id()));
    }

}
