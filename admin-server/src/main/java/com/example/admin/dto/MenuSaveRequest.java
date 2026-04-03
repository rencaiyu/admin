package com.example.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuSaveRequest {
    private Long id;
    private Long parentId;
    @NotBlank(message = "名称不能为空")
    private String name;
    private String path;
    private String permission;
    @NotNull(message = "类型不能为空")
    private Integer type;
    @NotNull(message = "排序不能为空")
    private Integer sort;
    @NotNull(message = "状态不能为空")
    private Integer status;
}
