package com.inthefridges.api.repository;

import com.inthefridges.api.entity.MemberRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberRoleRepository {
    int save(MemberRole memberRole);
    List<String> findByMemberId(Long memberId);
}
