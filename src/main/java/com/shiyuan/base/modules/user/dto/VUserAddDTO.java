package com.shiyuan.base.modules.user.dto;

import com.shiyuan.base.common.dto.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VUserAddDTO extends BaseDTO {
    @NotBlank(message = "名称不能为空")
    private String name;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "电话不能为空")
    private String telephone;
    @NotNull(message = "角色不能为空")
    private Long role = 3L;
}
