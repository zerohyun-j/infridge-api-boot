package com.inthefridges.api.repository;

import com.inthefridges.api.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface CategoryRepository {
    Optional<Category> findById(int id);
}
