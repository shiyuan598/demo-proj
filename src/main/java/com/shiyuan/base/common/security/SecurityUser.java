package com.shiyuan.base.common.security;

import com.shiyuan.base.modules.user.VUser;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUser implements UserDetails {
    @Getter
    private final VUser user;
    @Getter
    private final String role;
    private final List<GrantedAuthority> authorities;

    public SecurityUser(VUser user, List<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
        this.role = extractRole(authorities);
    }

    // 提取角色的方法，假设角色以 "ROLE_" 开头
    private String extractRole(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).filter(auth -> auth.startsWith("ROLE_"))
            .map(auth -> auth.substring(5)) // 去掉 "ROLE_" 前缀
            .findFirst().orElse("USER"); // 默认角色
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
