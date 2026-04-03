package com.example.admin.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuNode {
    private Long id;
    private String name;
    private String path;
    private Integer type;
    private String permission;
    private Integer sort;
    private List<MenuNode> children = new ArrayList<>();
}
