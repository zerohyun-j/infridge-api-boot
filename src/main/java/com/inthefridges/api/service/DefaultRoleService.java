package com.inthefridges.api.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultRoleService implements RoleService{
    @Override
    public List<GrantedAuthority> getList(Long memberId) {
        return null;
    }
}
