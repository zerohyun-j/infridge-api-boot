package com.inthefridges.api.controller.file;

import com.inthefridges.api.dto.response.FileResponse;
import com.inthefridges.api.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files/")
@RequiredArgsConstructor
public class FileController {

    private final FileService service;

    @GetMapping("upload/{id}")
    public ResponseEntity<?> get(@PathVariable int id){
        return ResponseEntity.ok(null);
    }

    @PostMapping("upload")
    public ResponseEntity<?> create(@RequestPart(value="file") MultipartFile file){
        Long fileId = service.create(file);
        Map<String, Object> response = new HashMap<>();
        response.put("fileId", fileId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("upload/{id}")
    public ResponseEntity<FileResponse> update(@PathVariable int id, @RequestPart(value="file") MultipartFile file){
        return null;
    }

    @DeleteMapping("upload/{id}")
    public ResponseEntity<FileResponse> delete(@PathVariable int id){
        return null;
    }
}
