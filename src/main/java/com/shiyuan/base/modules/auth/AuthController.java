package com.shiyuan.base.modules.auth;

import com.shiyuan.base.common.log.annotation.OperationLog;
import com.shiyuan.base.common.response.ResponseResult;
import com.shiyuan.base.modules.auth.dto.ForgetPasswordDTO;
import com.shiyuan.base.modules.auth.dto.LoginDTO;
import com.shiyuan.base.modules.user.dto.VUserAddDTO;
import com.shiyuan.base.modules.user.vo.VUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "认证相关")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @OperationLog("用户登录")
    @Operation(summary = "登录")
    @PostMapping("/login")
    public ResponseEntity<ResponseResult<VUserVO>> login(@Parameter(description = "登录信息") @RequestBody LoginDTO loginDTO) {
        VUserVO userVO = authService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return ResponseEntity.ok(ResponseResult.success(userVO));
    }

    @OperationLog("用户注册")
    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public ResponseEntity<ResponseResult<VUserVO>>
        register(@Parameter(description = "用户信息") @RequestBody VUserAddDTO user) {
        VUserVO userVO = authService.register(user);
        return ResponseEntity.ok(ResponseResult.success(userVO));
    }

    @OperationLog("重置密码")
    @Operation(summary = "忘记密码")
    @PostMapping("/forgetPassword")
    public ResponseEntity<ResponseResult<Boolean>> forgetPassword(
        @Parameter(description = "忘记密码信息") @RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        authService.forgetPassword(forgetPasswordDTO.getUsername(), forgetPasswordDTO.getTelephone(), forgetPasswordDTO.getNewPassword());
        return ResponseEntity.ok(ResponseResult.success(true));
    }

}
