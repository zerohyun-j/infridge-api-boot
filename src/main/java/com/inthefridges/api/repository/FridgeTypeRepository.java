package com.inthefridges.api.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FridgeTypeRepository {
    int create(Long fridgeId, Long typeId);
}
