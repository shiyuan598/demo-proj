package com.shiyuan.base.modules.auth;

import lombok.Data;

@Data
public class LoginUser {
    private Long id;
    private String username;
    private String role;
}