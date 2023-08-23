package com.inthefridges.api.repository;

import com.inthefridges.api.entity.Fridge;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FridgeRepository {
    List<Fridge> findByMemberId(Long memberId);
    Optional<Fridge> findById(Long id);
    int create(Fridge fridge);
    int update(Fridge fridge);
    int delete(Long id);
}
