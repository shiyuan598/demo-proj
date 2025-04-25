package com.shiyuan.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.shiyuan.base.entity.VUser;
import com.shiyuan.base.entity.converter.UserConverter;
import com.shiyuan.base.entity.vo.user.VUserVO;
import com.shiyuan.base.mapper.VUserMapper;
import com.shiyuan.base.service.VUserService;
import com.shiyuan.base.util.PageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
    @Autowired
    private UserConverter userConverter;

    @Override
    public boolean forgetPassword(String username, String telephone, String newPassword) {
        LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VUser::getUsername, username).eq(VUser::getTelephone, telephone);
        VUser user = this.getOne(wrapper);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            return updateById(user);
        }
        return false;
    }

    @Override
    public IPage<VUserVO> getUserPage(String blurry, long currentPage, long pageSize, String sort, String order) {
        LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(blurry)) {
            wrapper.like(VUser::getName, blurry).or().like(VUser::getUsername, blurry);
        }

        boolean isAsc = !"desc".equalsIgnoreCase(order);
        switch (Objects.toString(sort, "id").toLowerCase()) {
            case "username" -> wrapper.orderBy(true, isAsc, VUser::getUsername);
            case "role"     -> wrapper.orderBy(true, isAsc, VUser::getRole);
            case "name"     -> wrapper.orderBy(true, isAsc, VUser::getName);
            default         -> wrapper.orderByDesc(VUser::getId);
        }

        IPage<VUser> page = this.page(new Page<>(currentPage, pageSize), wrapper);
        return PageConverter.convert(page, userConverter::toVO);
    }
}




