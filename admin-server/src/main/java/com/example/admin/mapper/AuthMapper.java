package com.example.admin.mapper;

import com.example.admin.entity.SysMenu;
import com.example.admin.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthMapper {
    List<SysRole> findRolesByUserId(@Param("userId") Long userId);

    List<SysMenu> findMenusByRoleIds(@Param("roleIds") List<Long> roleIds);

    List<Long> findRoleIdsByUserId(@Param("userId") Long userId);

    List<Long> findMenuIdsByRoleId(@Param("roleId") Long roleId);

    List<Long> findRoleIdsByUser(@Param("userId") Long userId);
}
