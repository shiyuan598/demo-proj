package com.shiyuan.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @TableName v_user
 */
@TableName(value ="v_user")
@Data
public class AppUser {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String username;

    @JsonIgnore
    private String password;

    private String telephone;

    private Integer role;

    @JsonIgnore
    private String token;
}