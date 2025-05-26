package com.shiyuan.base.common.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof JwtUserInfo jwtUserInfo) {
            return jwtUserInfo.getUsername();
        }
        return "anonymous";
    }

    public static Long getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof JwtUserInfo jwtUserInfo) {
            return jwtUserInfo.getId();
        }
        return null;
    }
}
