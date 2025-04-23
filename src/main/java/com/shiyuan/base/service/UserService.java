package com.shiyuan.base.service;

import com.shiyuan.base.entity.AppUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author wangshiyuan
* @description 针对表【v_user】的数据库操作Service
* @createDate 2025-04-22 16:42:19
*/
public interface UserService extends IService<AppUser> {
    boolean forgetPassword(String username, String telephone, String newPassword);
}
