package com.inthefridges.api.repository;

import com.inthefridges.api.entity.StorageType;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface StorageTypeRepository {
    Optional<StorageType> findById(int id);
}
