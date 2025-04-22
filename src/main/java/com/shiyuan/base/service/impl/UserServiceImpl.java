package com.shiyuan.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.entity.User;
import com.shiyuan.base.service.UserService;
import com.shiyuan.base.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author wangshiyuan
* @description 针对表【v_user】的数据库操作Service实现
* @createDate 2025-04-22 16:42:19
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




