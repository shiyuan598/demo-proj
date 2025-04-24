package com.shiyuan.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.entity.VUser;
import com.shiyuan.base.mapper.VUserMapper;
import com.shiyuan.base.service.VUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
* @author wangshiyuan
* @description 针对表【v_user】的数据库操作Service实现
* @createDate 2025-04-24 13:40:42
*/
@Service
public class VUserServiceImpl extends ServiceImpl<VUserMapper, VUser>
    implements VUserService{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean forgetPassword(String username, String telephone, String newPassword) {
        LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VUser::getUsername, username).eq(VUser::getTelephone, telephone);
        VUser user = getOne(wrapper);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            return updateById(user);
        }
        return false;
    }
}




