package com.shiyuan.base.common.security;

import java.util.List;
import lombok.Data;

@Data
public class JwtUserInfo {
    private Long id;
    private String username;
    private String role;
    private List<String> authorities;
}
