package com.example.admin.service;

import com.example.admin.dto.MenuNode;
import com.example.admin.dto.MenuSaveRequest;
import com.example.admin.entity.SysMenu;

import java.util.List;

public interface MenuService {
    List<SysMenu> listMenus(String keyword);

    void saveMenu(MenuSaveRequest request);

    void deleteMenu(Long id);

    List<MenuNode> menuTree(List<SysMenu> menus);
}
