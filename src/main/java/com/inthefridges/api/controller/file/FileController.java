package com.inthefridges.api.controller.file;

import com.inthefridges.api.dto.response.FileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files/")
public class FileController {

    @GetMapping("images/profile")
    public ResponseEntity<FileResponse> getProfileImage(){
        return null;
    }

    @PutMapping("images/profile")
    public ResponseEntity<FileResponse> updateProfileImage(){
        return null;
    }

    @DeleteMapping("images/profile")
    public ResponseEntity<FileResponse> deleteProfileImage(){
        return null;
    }

    @GetMapping("images/fridge")
    public ResponseEntity<FileResponse> getFridgeImage(){
        return null;
    }

    @PostMapping("images/fridge")
    public ResponseEntity<FileResponse> createFridgeImage(){
        return null;
    }

    @PutMapping("images/fridge")
    public ResponseEntity<FileResponse> updateFridgeImage(){
        return null;
    }

    @DeleteMapping("images/fridge")
    public ResponseEntity<FileResponse> deleteFridgeImage(){
        return null;
    }
}
