package com.shiyuan.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.entity.AppUser;
import com.shiyuan.base.service.UserService;
import com.shiyuan.base.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
* @author wangshiyuan
* @description 针对表【v_user】的数据库操作Service实现
* @createDate 2025-04-22 16:42:19
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, AppUser>
    implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean forgetPassword(String username, String telephone, String newPassword) {
        LambdaQueryWrapper<AppUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AppUser::getUsername, username).eq(AppUser::getTelephone, telephone);
        AppUser user = getOne(wrapper);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            return updateById(user);
        }
        return false;
    }
}




