package com.shiyuan.base.modules.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户")
public class VUserVO {
    private Integer id;
    private String name;
    private String username;
    private String telephone;
    private Integer role;
}
