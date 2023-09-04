package com.inthefridges.api.repository;

import com.inthefridges.api.entity.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileRepository {
    void save(File file);
}
