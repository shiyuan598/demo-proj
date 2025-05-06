package com.shiyuan.base.common.security;

import lombok.Data;

import java.util.List;

@Data
public class JwtUserInfo {
    private Long id;
    private String username;
    private String role;
    private List<String> authorities;
}