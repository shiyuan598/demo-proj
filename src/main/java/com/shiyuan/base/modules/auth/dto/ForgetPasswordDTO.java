package com.shiyuan.base.modules.auth.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgetPasswordDTO {
    @Parameter(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Parameter(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String telephone;

    @Parameter(description = "新密码")
    @NotBlank(message = "密码不能为空")
    private String newPassword;
}
