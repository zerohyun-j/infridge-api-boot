package com.inthefridges.api.controller.file;

import com.inthefridges.api.dto.request.FileRequest;
import com.inthefridges.api.dto.response.FileResponse;
import com.inthefridges.api.global.security.jwt.model.JwtAuthentication;
import com.inthefridges.api.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService service;

    @GetMapping
    public ResponseEntity<FileResponse> get(@AuthenticationPrincipal JwtAuthentication member, @RequestBody FileRequest fileRequest){
        FileResponse fileResponse = service.get(member.id(), fileRequest);
        return ResponseEntity.ok(fileResponse);
    }

    @PostMapping
    public ResponseEntity<FileResponse> create(@AuthenticationPrincipal JwtAuthentication member, @RequestPart(value="file") MultipartFile file){
        FileResponse fileResponse = service.create(member.id(), file);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileResponse);
    }

    @PutMapping
    public ResponseEntity<FileResponse> update(@AuthenticationPrincipal JwtAuthentication member, @RequestBody FileRequest fileRequest){
        FileResponse fileResponse = service.update(member.id(), fileRequest);
        return ResponseEntity.ok(fileResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal JwtAuthentication member, @RequestBody FileRequest fileRequest){
        service.delete(member.id(), fileRequest);
        return ResponseEntity.noContent().build();
    }
}
