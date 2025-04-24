package com.shiyuan.base.service;

import com.shiyuan.base.entity.VUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author wangshiyuan
* @description 针对表【v_user】的数据库操作Service
* @createDate 2025-04-24 13:40:42
*/
public interface VUserService extends IService<VUser> {

    boolean forgetPassword(String username, String telephone, String newPassword);
}
