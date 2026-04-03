package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.admin.dto.UserSaveRequest;
import com.example.admin.entity.SysRole;
import com.example.admin.entity.SysUser;
import com.example.admin.entity.SysUserRole;
import com.example.admin.mapper.AuthMapper;
import com.example.admin.mapper.RoleMapper;
import com.example.admin.mapper.UserMapper;
import com.example.admin.mapper.UserRoleMapper;
import com.example.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final AuthMapper authMapper;

    @Override
    public List<SysUser> listUsers(String keyword) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.like(SysUser::getUsername, keyword).or().like(SysUser::getNickname, keyword);
        }
        wrapper.orderByDesc(SysUser::getId);
        return userMapper.selectList(wrapper);
    }

    @Override
    public void saveUser(UserSaveRequest request) {
        SysUser user = new SysUser();
        user.setId(request.getId());
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname());
        user.setStatus(request.getStatus());
        user.setUpdateTime(LocalDateTime.now());

        if (request.getId() == null) {
            user.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8)));
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);
        } else {
            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                user.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8)));
            }
            userMapper.updateById(user);
            userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, request.getId()));
        }

        Long userId = request.getId() == null ? user.getId() : request.getId();
        if (request.getRoleIds() != null) {
            request.getRoleIds().forEach(roleId -> {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                userRoleMapper.insert(ur);
            });
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        userMapper.deleteById(id);
    }

    @Override
    public List<Long> getUserRoleIds(Long userId) {
        return authMapper.findRoleIdsByUser(userId);
    }

    @Override
    public Map<Long, String> roleNameMap() {
        return roleMapper.selectList(new LambdaQueryWrapper<SysRole>()).stream()
                .collect(Collectors.toMap(SysRole::getId, SysRole::getRoleName, (a, b) -> a));
    }
}
