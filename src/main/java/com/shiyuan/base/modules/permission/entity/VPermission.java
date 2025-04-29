package com.shiyuan.base.modules.permission.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_permission
 */
@TableName(value ="v_permission")
@Data
public class VPermission {
    private Long id;

    private String name;

    private String url;

    private String method;

    private String code;
}