package com.shiyuan.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_user
 */
@TableName(value ="v_user")
@Data
public class User {
    private Integer id;

    private String name;

    private String username;

    private String password;

    private String telephone;

    private Integer role;

    private String token;
}