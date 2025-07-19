package com.shiyuan.base.modules.auth.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @Parameter(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;
    @Parameter(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
