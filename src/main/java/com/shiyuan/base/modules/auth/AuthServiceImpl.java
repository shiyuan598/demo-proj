package com.shiyuan.base.modules.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiyuan.base.common.exception.AuthenticationFailedException;
import com.shiyuan.base.common.security.JwtUserInfo;
import com.shiyuan.base.common.security.SecurityUser;
import com.shiyuan.base.common.utils.JwtUtils;
import com.shiyuan.base.modules.permission.entity.VUserRole;
import com.shiyuan.base.modules.permission.service.VRoleService;
import com.shiyuan.base.modules.permission.service.VUserRoleService;
import com.shiyuan.base.modules.user.UserConverter;
import com.shiyuan.base.modules.user.VUser;
import com.shiyuan.base.modules.user.VUserService;
import com.shiyuan.base.modules.user.dto.VUserAddDTO;
import com.shiyuan.base.modules.user.vo.VUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

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
        try {
            // 调用 Spring Security 验证用户名密码（UserDetailsServiceImpl）
            // 调用 UserDetailsService#loadUserByUsername 方法从数据库中加载用户的详细信息
            // 使用 passwordEncoder 对用户输入的密码和数据库中存储的加密密码进行比较，验证密码的有效性
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // 拿到认证后的用户
            SecurityUser securityUser = (SecurityUser) auth.getPrincipal();
            String role = securityUser.getRole();

            // 提取所有权限字符串
            List<String> authorityList = securityUser.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // 构建并返回登录结果
            return buildLoginUserVO(securityUser.getUser(), role, authorityList);
        } catch(AuthenticationException e) {
            throw new AuthenticationFailedException("用户名或密码错误");
        }
    }

    @Override
    public VUserVO register(VUserAddDTO userAddDTO) {
        LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VUser::getUsername, userAddDTO.getUsername());
        if (userService.count(wrapper) > 0) {
            throw new IllegalArgumentException("用户已存在");
        }

        VUser user = userConverter.toEntity(userAddDTO);
        user.setPassword(passwordEncoder.encode(userAddDTO.getPassword()));
        userService.save(user);

        // 默认分配普通用户角色
        VUserRole userRole = new VUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(userAddDTO.getRole());
        userRoleService.save(userRole);

        // 返回用户信息
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
        List<String> authorities = new ArrayList<>();
        authorities.add("ROLE_" + role);

        return buildLoginUserVO(user, role, authorities);
    }

    private VUserVO buildLoginUserVO(VUser user, String role, List<String> authorities) {
        JwtUserInfo jwtUserInfo = new JwtUserInfo();
        jwtUserInfo.setId(user.getId());
        jwtUserInfo.setUsername(user.getUsername());
        jwtUserInfo.setRole(role);
        jwtUserInfo.setAuthorities(authorities);

        String token = jwtUtils.generateToken(jwtUserInfo);

        VUserVO userVO = userConverter.toVO(user);
        userVO.setToken(token);
        userVO.setRole(role);
        return userVO;
    }
}