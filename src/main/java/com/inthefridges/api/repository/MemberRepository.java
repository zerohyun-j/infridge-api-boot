package com.inthefridges.api.repository;

import com.inthefridges.api.entity.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberRepository {
    Optional<Member> findByMemberId(Long id);
    Optional<Member> findBySocialId(String id);
    int save(Member Member);
}
