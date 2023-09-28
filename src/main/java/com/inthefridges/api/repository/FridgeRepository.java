package com.inthefridges.api.repository;

import com.inthefridges.api.entity.Fridge;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FridgeRepository {
    List<Fridge> findFridgesByMemberId(Long memberId);
    Optional<Fridge> findById(Long id);
    int save(Fridge fridge);
    int update(Fridge fridge);
    int delete(Long id);
}
