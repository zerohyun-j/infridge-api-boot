package com.inthefridges.api.service;

import com.inthefridges.api.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultMemberRoleService implements MemberRoleService{
    private final MemberRoleRepository repository;
    @Override
    public List<String> getList(Long memberId) {
        return repository.findByMemberId(memberId);
    }
}
