package com.shiyuan.base.modules.auth;

import com.shiyuan.base.modules.user.dto.VUserAddDTO;
import com.shiyuan.base.modules.user.vo.VUserVO;

public interface AuthService {
    VUserVO login(String username, String password);

    VUserVO register(VUserAddDTO userAddDTO);

    Boolean forgetPassword(String username, String telephone, String newPassword);
}
