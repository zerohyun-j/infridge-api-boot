package com.inthefridges.api.repository;

import com.inthefridges.api.entity.InFridgeFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface FileRepository {
    void save(InFridgeFile file);
    Optional<InFridgeFile> findById(InFridgeFile file);
    Optional<InFridgeFile> findByMemberId(Long memberId);
    Optional<InFridgeFile> findByIdAndPostId(InFridgeFile file);
    Optional<InFridgeFile> findByFridgeId(Long fridgeId);
    Optional<InFridgeFile> findByItemId(Long itemId);
    int update(InFridgeFile file);
    void delete(InFridgeFile file);
}
