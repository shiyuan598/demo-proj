package com.shiyuan.base.entity.dto;

import lombok.Data;

@Data
public class VUserDTO {
    private Integer id;

    private String name;

    private String username;

    private String password;

    private String telephone;

    private Integer role;
}
