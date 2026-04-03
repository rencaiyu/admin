package com.example.admin.service;

import com.example.admin.dto.LoginRequest;
import com.example.admin.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    LoginResponse buildUserPermissionInfo(Long userId, String token);
}
