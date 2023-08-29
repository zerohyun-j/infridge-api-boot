package com.inthefridges.api.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void create(MultipartFile file);

}
