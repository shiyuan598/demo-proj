package com.shiyuan.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiyuan.base.entity.AppUser;
import com.shiyuan.base.service.UserService;
import com.shiyuan.base.util.JwtUtils;
import com.shiyuan.base.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证相关")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public ResponseEntity<ResponseUtils> login(@Parameter(description = "用户名") @RequestParam String username, @Parameter(description = "密码") @RequestParam String password) {
        try {
            // 查询用户信息
            LambdaQueryWrapper<AppUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AppUser::getUsername, username);
            AppUser appUser =  userService.getOne(wrapper);
            if (appUser != null && passwordEncoder.matches(password, appUser.getPassword())) {
                // 密码匹配，生成 JWT Token
                String token = jwtUtils.generateToken(username);
                appUser.setPassword(""); // 抹掉密码
                appUser.setToken(token);
                return ResponseUtils.success(appUser);
            } else {
                return ResponseUtils.fail(400,"用户名或密码错误");
            }
        } catch (Exception e) {
            return ResponseUtils.error(e.getMessage());
        }
    }

    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public ResponseEntity<ResponseUtils> register(@Parameter(description = "用户信息") @RequestBody AppUser appUser) {
        try {
            // 检查用户名是否已存在
            LambdaQueryWrapper<AppUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AppUser::getUsername, appUser.getUsername());
            long count = userService.count(wrapper);
            if (count > 0) {
                return ResponseUtils.fail(400,"用户名已存在");
            }

            // 注册用户
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword())); // 加密密码
            userService.save(appUser);
            // 生成 JWT Token
            String token = jwtUtils.generateToken(appUser.getUsername());
            appUser.setPassword(""); // 抹掉密码
            appUser.setToken(token);
            return ResponseUtils.success(appUser);
        } catch (Exception e) {
            return ResponseUtils.error(e.getMessage());
        }
    }

    @Operation(summary = "忘记密码")
    @PostMapping("/forgetPassword")
    public ResponseEntity<ResponseUtils> forgetPassword(
            @Parameter(description = "用户名") @RequestParam String username,
            @Parameter(description = "手机号") @RequestParam String telephone,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        try {
            boolean result = userService.forgetPassword(username, telephone, newPassword);
            if (result) {
                return ResponseUtils.success("密码修改成功");
            } else {
                return ResponseUtils.fail(400, "用户名或手机号不正确，密码修改失败");
            }
        } catch (Exception e) {
            return ResponseUtils.error(e.getMessage());
        }
    }
}
