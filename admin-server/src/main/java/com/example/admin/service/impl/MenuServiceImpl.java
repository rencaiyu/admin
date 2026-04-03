package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.admin.dto.MenuNode;
import com.example.admin.dto.MenuSaveRequest;
import com.example.admin.entity.SysMenu;
import com.example.admin.mapper.MenuMapper;
import com.example.admin.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;

    @Override
    public List<SysMenu> listMenus(String keyword) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.like(SysMenu::getName, keyword);
        }
        wrapper.orderByAsc(SysMenu::getSort).orderByAsc(SysMenu::getId);
        return menuMapper.selectList(wrapper);
    }

    @Override
    public void saveMenu(MenuSaveRequest request) {
        SysMenu menu = new SysMenu();
        menu.setId(request.getId());
        menu.setParentId(request.getParentId() == null ? 0L : request.getParentId());
        menu.setName(request.getName());
        menu.setPath(request.getPath());
        menu.setPermission(request.getPermission());
        menu.setType(request.getType());
        menu.setSort(request.getSort());
        menu.setStatus(request.getStatus());
        menu.setUpdateTime(LocalDateTime.now());
        if (request.getId() == null) {
            menu.setCreateTime(LocalDateTime.now());
            menuMapper.insert(menu);
        } else {
            menuMapper.updateById(menu);
        }
    }

    @Override
    public void deleteMenu(Long id) {
        menuMapper.deleteById(id);
    }

    @Override
    public List<MenuNode> menuTree(List<SysMenu> menus) {
        Map<Long, MenuNode> nodeMap = new HashMap<>();
        List<MenuNode> roots = new ArrayList<>();
        menus.forEach(menu -> {
            MenuNode node = new MenuNode();
            node.setId(menu.getId());
            node.setName(menu.getName());
            node.setPath(menu.getPath());
            node.setPermission(menu.getPermission());
            node.setSort(menu.getSort());
            node.setType(menu.getType());
            nodeMap.put(menu.getId(), node);
        });
        menus.forEach(menu -> {
            MenuNode current = nodeMap.get(menu.getId());
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                roots.add(current);
            } else {
                MenuNode parent = nodeMap.get(menu.getParentId());
                if (parent != null) {
                    parent.getChildren().add(current);
                } else {
                    roots.add(current);
                }
            }
        });
        sortNodes(roots);
        return roots;
    }

    private void sortNodes(List<MenuNode> nodes) {
        nodes.sort(Comparator.comparing(MenuNode::getSort).thenComparing(MenuNode::getId));
        nodes.forEach(n -> sortNodes(n.getChildren()));
    }
}
