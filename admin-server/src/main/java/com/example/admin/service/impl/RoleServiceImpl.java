package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.admin.dto.RoleSaveRequest;
import com.example.admin.entity.SysRole;
import com.example.admin.entity.SysRoleMenu;
import com.example.admin.mapper.AuthMapper;
import com.example.admin.mapper.RoleMapper;
import com.example.admin.mapper.RoleMenuMapper;
import com.example.admin.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final AuthMapper authMapper;

    @Override
    public List<SysRole> listRoles(String keyword) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.like(SysRole::getRoleName, keyword).or().like(SysRole::getRoleCode, keyword);
        }
        wrapper.orderByDesc(SysRole::getId);
        return roleMapper.selectList(wrapper);
    }

    @Override
    public void saveRole(RoleSaveRequest request) {
        SysRole role = new SysRole();
        role.setId(request.getId());
        role.setRoleCode(request.getRoleCode());
        role.setRoleName(request.getRoleName());
        role.setStatus(request.getStatus());
        role.setUpdateTime(LocalDateTime.now());
        if (request.getId() == null) {
            role.setCreateTime(LocalDateTime.now());
            roleMapper.insert(role);
        } else {
            roleMapper.updateById(role);
            roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, request.getId()));
        }

        Long roleId = request.getId() == null ? role.getId() : request.getId();
        if (request.getMenuIds() != null) {
            request.getMenuIds().forEach(menuId -> {
                SysRoleMenu rm = new SysRoleMenu();
                rm.setRoleId(roleId);
                rm.setMenuId(menuId);
                roleMenuMapper.insert(rm);
            });
        }
    }

    @Override
    public void deleteRole(Long id) {
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
        roleMapper.deleteById(id);
    }

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        return authMapper.findMenuIdsByRoleId(roleId);
    }
}
