package com.inthefridges.api.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface RoleService {
    List<GrantedAuthority> getList(Long memberId);
}
