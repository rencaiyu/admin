package com.example.admin.service;

import com.example.admin.dto.UserSaveRequest;
import com.example.admin.entity.SysUser;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<SysUser> listUsers(String keyword);

    void saveUser(UserSaveRequest request);

    void deleteUser(Long id);

    List<Long> getUserRoleIds(Long userId);

    Map<Long, String> roleNameMap();
}
