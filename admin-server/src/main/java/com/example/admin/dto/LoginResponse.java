package com.example.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private String token;
    private UserInfo userInfo;
    private List<MenuNode> menus;
    private List<String> permissions;

    @Data
    public static class UserInfo {
        private Long userId;
        private String username;
        private String nickname;
        private List<String> roleCodes;
    }
}
