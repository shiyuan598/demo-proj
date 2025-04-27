package com.shiyuan.base.modules.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_user
 */
@TableName(value ="v_user")
@Data
public class VUser {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String username;

    private String password;

    private String telephone;

    private Integer role;

    private String token;
}