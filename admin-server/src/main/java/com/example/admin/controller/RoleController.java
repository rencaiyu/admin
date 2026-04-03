package com.example.admin.controller;

import com.example.admin.common.ApiResponse;
import com.example.admin.dto.RoleSaveRequest;
import com.example.admin.entity.SysRole;
import com.example.admin.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ApiResponse<List<SysRole>> list(@RequestParam(required = false) String keyword) {
        return ApiResponse.success(roleService.listRoles(keyword));
    }

    @PostMapping
    public ApiResponse<Void> save(@Valid @RequestBody RoleSaveRequest request) {
        roleService.saveRole(request);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.success();
    }

    @GetMapping("/{id}/menus")
    public ApiResponse<List<Long>> roleMenus(@PathVariable Long id) {
        return ApiResponse.success(roleService.getRoleMenuIds(id));
    }
}
