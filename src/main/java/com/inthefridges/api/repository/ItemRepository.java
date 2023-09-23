package com.inthefridges.api.repository;

import com.inthefridges.api.entity.Item;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ItemRepository {
    int save(Item item);
    Optional<Item> findById(Long id);
    Optional<Item> findByIdAndFridgeId(Long id, Long fridgeId);
    Optional<Item> findByFridgeId(Long fridgeId);
    List<Item> findItemsByFridgeIdAndStorageId(Long fridgeId, int storageId);
    int update(Item item);
    void delete(Long id, Long fridgeId);
}
