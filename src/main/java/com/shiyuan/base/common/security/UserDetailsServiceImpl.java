package com.shiyuan.base.common.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiyuan.base.modules.permission.service.VRoleService;
import com.shiyuan.base.modules.user.VUser;
import com.shiyuan.base.modules.user.VUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private VUserService userService;

    @Autowired
    private VRoleService roleService;

    public UserDetailsServiceImpl(VUserService userService) {
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VUser::getUsername, username);
        VUser user = userService.getOne(wrapper);

        if (user == null) {
            throw new UsernameNotFoundException("用户 " + username + " 不存在");
        }

        // 构建用户权限集合
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 用户角色信息
        String role = "ROLE_" + roleService.getRoleCodeByUserId(user.getId());
        authorities.add(new SimpleGrantedAuthority(role));

        return new SecurityUser(user, authorities);
    }
}