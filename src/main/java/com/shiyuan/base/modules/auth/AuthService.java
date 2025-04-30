package com.shiyuan.base.modules.auth;

import com.shiyuan.base.modules.user.VUser;
import com.shiyuan.base.modules.user.vo.VUserVO;

public interface AuthService {
    VUserVO login(String username, String password);
    VUserVO register(VUser user);
    Boolean forgetPassword(String username, String telephone, String newPassword);
}

