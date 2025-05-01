package com.shiyuan.base.modules.auth;

import com.shiyuan.base.common.response.ResponseResult;
import com.shiyuan.base.common.response.ResultCode;
import com.shiyuan.base.modules.user.dto.VUserAddDTO;
import com.shiyuan.base.modules.user.vo.VUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证相关")
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public ResponseEntity<ResponseResult<VUserVO>> login(@Parameter(description = "用户名") @RequestParam String username, @Parameter(description = "密码") @RequestParam String password) {
        try {
            VUserVO userVO = authService.login(username, password);
            return ResponseEntity.ok(ResponseResult.success(userVO));
        } catch(IllegalArgumentException e) {
            log.error("登录失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.fail(ResultCode.PARAM_ERROR, e.getMessage()));
        } catch (Exception e) {
            log.error("登录处理异常: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public ResponseEntity<ResponseResult<VUserVO>> register(@Parameter(description = "用户信息") @RequestBody VUserAddDTO user) {
        try {
            VUserVO userVO = authService.register(user);
            return ResponseEntity.ok(ResponseResult.success(userVO));
        } catch(IllegalArgumentException e) {
            log.error("注册失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.fail(ResultCode.PARAM_ERROR, e.getMessage()));
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
            authService.forgetPassword(username, telephone, newPassword);
            return ResponseEntity.ok(ResponseResult.success(true));
        } catch(IllegalArgumentException e) {
            log.error("注册失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.fail(ResultCode.PARAM_ERROR, e.getMessage()));
        } catch (Exception e) {
            log.error("忘记密码异常: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }
}