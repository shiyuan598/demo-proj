package com.shiyuan.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiyuan.base.entity.VUser;
import com.shiyuan.base.service.VUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private VUserService userService;

    public UserDetailsServiceImpl(VUserService userService) {
        this.userService = userService;
        logger.info("UserDetailsServiceImpl initialized with UserService: {}", userService.getClass().getName());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VUser::getUsername, username);
        VUser vAppUser = userService.getOne(wrapper);

        if (vAppUser == null) {
            throw new UsernameNotFoundException("用户 " + username + " 不存在");
        }

        // 构建用户权限集合
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 这里简单假设用户角色信息存储在 VUser 的 role 字段，可根据实际情况调整
        String role = "ROLE_" + vAppUser.getRole();
        authorities.add(new SimpleGrantedAuthority(role));

        // 构建 Spring Security 的 UserDetails 对象
        return new User(
                vAppUser.getUsername(),
                vAppUser.getPassword(),
                true, // 是否启用
                true, // 账户是否未过期
                true, // 密码是否未过期
                true, // 账户是否未锁定
                authorities
        );
    }
}