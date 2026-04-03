package com.example.admin.service;

import com.example.admin.dto.RoleSaveRequest;
import com.example.admin.entity.SysRole;

import java.util.List;

public interface RoleService {
    List<SysRole> listRoles(String keyword);

    void saveRole(RoleSaveRequest request);

    void deleteRole(Long id);

    List<Long> getRoleMenuIds(Long roleId);
}
