package com.inthefridges.api.repository;

import com.inthefridges.api.entity.InFridgeFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileRepository {
    void save(InFridgeFile file);
}
