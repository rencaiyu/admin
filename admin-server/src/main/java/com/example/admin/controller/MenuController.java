package com.example.admin.controller;

import com.example.admin.common.ApiResponse;
import com.example.admin.dto.MenuSaveRequest;
import com.example.admin.entity.SysMenu;
import com.example.admin.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ApiResponse<List<SysMenu>> list(@RequestParam(required = false) String keyword) {
        return ApiResponse.success(menuService.listMenus(keyword));
    }

    @PostMapping
    public ApiResponse<Void> save(@Valid @RequestBody MenuSaveRequest request) {
        menuService.saveMenu(request);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ApiResponse.success();
    }
}
