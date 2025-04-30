package com.shiyuan.base.modules.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiyuan.base.common.utils.JwtUtils;
import com.shiyuan.base.modules.permission.entity.VUserRole;
import com.shiyuan.base.modules.permission.service.VRoleService;
import com.shiyuan.base.modules.permission.service.VUserRoleService;
import com.shiyuan.base.modules.user.UserConverter;
import com.shiyuan.base.modules.user.VUser;
import com.shiyuan.base.modules.user.VUserService;
import com.shiyuan.base.modules.user.vo.VUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private VUserService userService;

    @Autowired
    private VRoleService roleService;

    @Autowired
    private VUserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public VUserVO login(String username, String password) {
        LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VUser::getUsername, username);
        VUser user = userService.getOne(wrapper);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return buildLoginUserVO(user);
        }

        throw new IllegalArgumentException("用户名或密码错误");
    }

    @Override
    public VUserVO register(VUser user) {
        LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VUser::getUsername, user.getUsername());
        if (userService.count(wrapper) > 0) {
            throw new IllegalArgumentException("用户已存在");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);

        VUserRole userRole = new VUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(3L); // 普通用户
        userRoleService.save(userRole);

        return buildLoginUserVO(user);
    }

    @Override
    public Boolean forgetPassword(String username, String telephone, String newPassword) {
        boolean result = userService.forgetPassword(username, telephone, newPassword);
        if (result) {
            return true;
        }
        throw new IllegalArgumentException("用户名或手机号错误");
    }

    private VUserVO buildLoginUserVO(VUser user) {
        String role = roleService.getRoleCodeByUserId(user.getId());
        LoginUser loginUser = new LoginUser();
        loginUser.setId(user.getId());
        loginUser.setUsername(user.getUsername());
        loginUser.setRole(role);
        String token = jwtUtils.generateToken(loginUser);

        VUserVO vo = userConverter.toVO(user);
        vo.setToken(token);
        vo.setRole(role);
        return vo;
    }
}