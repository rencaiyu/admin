package com.example.admin.controller;

import com.example.admin.common.ApiResponse;
import com.example.admin.dto.UserSaveRequest;
import com.example.admin.entity.SysUser;
import com.example.admin.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<List<SysUser>> list(@RequestParam(required = false) String keyword) {
        return ApiResponse.success(userService.listUsers(keyword));
    }

    @PostMapping
    public ApiResponse<Void> save(@Valid @RequestBody UserSaveRequest request) {
        userService.saveUser(request);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success();
    }

    @GetMapping("/{id}/roles")
    public ApiResponse<List<Long>> userRoles(@PathVariable Long id) {
        return ApiResponse.success(userService.getUserRoleIds(id));
    }

    @GetMapping("/role-name-map")
    public ApiResponse<Map<Long, String>> roleNameMap() {
        return ApiResponse.success(userService.roleNameMap());
    }
}
