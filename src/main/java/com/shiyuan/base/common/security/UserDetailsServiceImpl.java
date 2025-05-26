package com.shiyuan.base.common.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiyuan.base.modules.permission.service.VRoleService;
import com.shiyuan.base.modules.user.VUser;
import com.shiyuan.base.modules.user.VUserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private VUserService userService;

    @Autowired
    private VRoleService roleService;

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
        // 使用用户角色信息区分权限，hasRole()判断角色时需要ROLE前缀
        String role = "ROLE_" + roleService.getRoleCodeByUserId(user.getId());
        authorities.add(new SimpleGrantedAuthority(role));

        return new SecurityUser(user, authorities);
    }
}
