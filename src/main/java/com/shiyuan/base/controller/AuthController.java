package com.shiyuan.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiyuan.base.entity.VUser;
import com.shiyuan.base.service.VUserService;
import com.shiyuan.base.util.JwtUtils;
import com.shiyuan.base.util.ResponseResult;
import com.shiyuan.base.util.ResultCode;
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
    public ResponseEntity<ResponseResult<VUser>> login(@Parameter(description = "用户名") @RequestParam String username, @Parameter(description = "密码") @RequestParam String password) {
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
                return ResponseEntity.ok(ResponseResult.success(user));
            } else {
                return ResponseEntity.badRequest().body(ResponseResult.fail(ResultCode.PARAM_ERROR));
            }
        } catch (Exception e) {
            log.error("登录处理异常: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public ResponseEntity<ResponseResult<VUser>> register(@Parameter(description = "用户信息") @RequestBody VUser user) {
        try {
            // 检查用户名是否已存在
            LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(VUser::getUsername, user.getUsername());
            long count = userService.count(wrapper);
            if (count > 0) {
                return ResponseEntity.badRequest().body(ResponseResult.fail(ResultCode.PARAM_ERROR, "用户已存在"));
            }

            // 注册用户
            user.setPassword(passwordEncoder.encode(user.getPassword())); // 加密密码
            userService.save(user);
            // 生成 JWT Token
            String token = jwtUtils.generateToken(user.getUsername());
            user.setPassword(""); // 抹掉密码
            user.setToken(token);
            return ResponseEntity.ok(ResponseResult.success(user));
        } catch (Exception e) {
            log.error("注册用户异常: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "忘记密码")
    @PostMapping("/forgetPassword")
    public ResponseEntity<ResponseResult<Boolean>> forgetPassword(
            @Parameter(description = "用户名") @RequestParam String username,
            @Parameter(description = "手机号") @RequestParam String telephone,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        try {
            boolean result = userService.forgetPassword(username, telephone, newPassword);
            if (result) {
                return ResponseEntity.ok(ResponseResult.success(result));
            } else {
                return ResponseEntity.badRequest().body(ResponseResult.fail(ResultCode.PARAM_ERROR));
            }
        } catch (Exception e) {
            log.error("忘记密码异常: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }
}
