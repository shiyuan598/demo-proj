package com.shiyuan.base.modules.user.dto;

import com.shiyuan.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VUserUpdateDTO extends BaseDTO {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String telephone;
    private Long role;
}
