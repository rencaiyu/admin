package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.admin.common.BizException;
import com.example.admin.dto.LoginRequest;
import com.example.admin.dto.LoginResponse;
import com.example.admin.dto.MenuNode;
import com.example.admin.entity.SysMenu;
import com.example.admin.entity.SysRole;
import com.example.admin.entity.SysUser;
import com.example.admin.mapper.AuthMapper;
import com.example.admin.mapper.UserMapper;
import com.example.admin.security.JwtUtils;
import com.example.admin.service.AuthService;
import com.example.admin.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final AuthMapper authMapper;
    private final MenuService menuService;
    private final JwtUtils jwtUtils;

    @Override
    public LoginResponse login(LoginRequest request) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, request.getUsername()));
        if (user == null || user.getStatus() != 1) {
            throw new BizException("用户不存在或已禁用");
        }
        String encrypted = DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!encrypted.equals(user.getPassword())) {
            throw new BizException("用户名或密码错误");
        }
        String token = jwtUtils.generateToken(user.getId());
        return buildUserPermissionInfo(user.getId(), token);
    }

    @Override
    public LoginResponse buildUserPermissionInfo(Long userId, String token) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        List<SysRole> roles = authMapper.findRolesByUserId(userId);
        List<Long> roleIds = roles.stream().map(SysRole::getId).toList();
        List<SysMenu> menus = roleIds.isEmpty() ? List.of() : authMapper.findMenusByRoleIds(roleIds);
        List<String> permissions = menus.stream()
                .filter(m -> m.getType() == 2 && m.getPermission() != null && !m.getPermission().isBlank())
                .map(SysMenu::getPermission)
                .distinct()
                .toList();

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setRoleCodes(roles.stream().map(SysRole::getRoleCode).toList());
        response.setUserInfo(userInfo);
        response.setMenus(menuService.menuTree(menus));
        response.setPermissions(permissions);
        return response;
    }
}
