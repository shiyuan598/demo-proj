package com.shiyuan.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiyuan.base.entity.VUser;
import com.shiyuan.base.service.VUserService;
import com.shiyuan.base.util.JwtUtils;
import com.shiyuan.base.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证相关")
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private VUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public ResponseEntity<ResponseUtils> login(@Parameter(description = "用户名") @RequestParam String username, @Parameter(description = "密码") @RequestParam String password) {
        try {
            // 查询用户信息
            LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(VUser::getUsername, username);
            VUser user =  userService.getOne(wrapper);
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                // 密码匹配，生成 JWT Token
                String token = jwtUtils.generateToken(username);
                user.setPassword(""); // 抹掉密码
                user.setToken(token);
                return ResponseUtils.success(user);
            } else {
                return ResponseUtils.fail(400,"用户名或密码错误");
            }
        } catch (Exception e) {
            log.error("登录处理异常: {}", e.getMessage(), e);
            return ResponseUtils.error(e.getMessage());
        }
    }

    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public ResponseEntity<ResponseUtils> register(@Parameter(description = "用户信息") @RequestBody VUser vUser) {
        try {
            // 检查用户名是否已存在
            LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(VUser::getUsername, vUser.getUsername());
            long count = userService.count(wrapper);
            if (count > 0) {
                return ResponseUtils.fail(400,"用户名已存在");
            }

            // 注册用户
            vUser.setPassword(passwordEncoder.encode(vUser.getPassword())); // 加密密码
            userService.save(vUser);
            // 生成 JWT Token
            String token = jwtUtils.generateToken(vUser.getUsername());
            vUser.setPassword(""); // 抹掉密码
            vUser.setToken(token);
            return ResponseUtils.success(vUser);
        } catch (Exception e) {
            log.error("注册用户异常: {}", e.getMessage(), e);
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
            log.error("忘记密码异常: {}", e.getMessage(), e);
            return ResponseUtils.error(e.getMessage());
        }
    }
}
