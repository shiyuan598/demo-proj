package com.shiyuan.base.modules.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.shiyuan.base.common.utils.PageConverter;
import com.shiyuan.base.modules.permission.entity.VUserRole;
import com.shiyuan.base.modules.permission.service.VUserRoleService;
import com.shiyuan.base.modules.user.dto.VUserAddDTO;
import com.shiyuan.base.modules.user.dto.VUserUpdateDTO;
import com.shiyuan.base.modules.user.mapper.VUserMapper;
import com.shiyuan.base.modules.user.vo.VUserVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangshiyuan
 * @description 针对表【v_user】的数据库操作Service实现
 * @createDate 2025-04-24 13:40:42
 */
@Service
@Primary
public class VUserServiceImpl extends ServiceImpl<VUserMapper, VUser> implements VUserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private VUserMapper userMapper;

    @Autowired
    private VUserRoleService userRoleService;

    @Transactional
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

    @Transactional(readOnly = true)
    @Override
    public List<VUserVO> getDrivers(String blurry) {
        return userMapper.getDrivers(blurry);
    }

    @Transactional(readOnly = true)
    @Override
    public IPage<VUserVO> getUserPage(String blurry, Long currentPage, Long pageSize, String sort, String order) {
        LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(blurry)) {
            wrapper.like(VUser::getName, blurry).or().like(VUser::getUsername, blurry);
        }

        String sortField = StrUtil.isBlank(sort) ? "id" : sort.toLowerCase();
        boolean isAsc = "asc".equalsIgnoreCase(order); // 明确指定 asc 才升序，默认是 desc

        switch (sortField) {
            case "id" -> wrapper.orderBy(true, isAsc, VUser::getId);
            case "username" -> wrapper.orderBy(true, isAsc, VUser::getUsername);
            case "name" -> wrapper.orderBy(true, isAsc, VUser::getName);
            default -> wrapper.orderByDesc(VUser::getId); // 兜底
        }

        IPage<VUser> pageData = this.page(new Page<>(currentPage, pageSize), wrapper);
        return PageConverter.convert(pageData, userConverter::toVO);
    }

    @Transactional
    @Override
    public Long addUser(VUserAddDTO userAddDTO) {
        VUser user = userConverter.toEntity(userAddDTO);
        user.setPassword(passwordEncoder.encode(userAddDTO.getPassword()));
        LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VUser::getUsername, userAddDTO.getUsername());
        if (this.exists(wrapper)) {
            throw new IllegalArgumentException("用户名已存在");
        }
        this.save(user);
        userRoleService.updateUserRole(user.getId(), userAddDTO.getRole());
        return user.getId();
    }

    @Transactional
    @Override
    public VUserVO updateUser(Long id, VUserUpdateDTO userUpdateDTO) {
        VUser existingUser = this.getById(id);
        if (existingUser == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        // 校验 username 是否已存在(除自己以外)
        if (userUpdateDTO.getUsername() != null) {
            LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(VUser::getUsername, userUpdateDTO.getUsername()).ne(VUser::getId, id);
            boolean userNoExists = this.exists(wrapper);
            if (userNoExists) {
                throw new IllegalArgumentException("用户名已存在");
            }
        }
        if (StrUtil.isNotBlank(userUpdateDTO.getPassword())) {
            userUpdateDTO.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }
        userConverter.updateEntityFromDto(userUpdateDTO, existingUser);
        this.updateById(existingUser);
        if (userUpdateDTO.getRole() != null) {
            userRoleService.updateUserRole(id, userUpdateDTO.getRole());
        }
        return userConverter.toVO(existingUser);
    }

    @Transactional
    @Override
    public boolean removeUserById(Long id) {
        userRoleService.remove(new LambdaQueryWrapper<VUserRole>().eq(VUserRole::getUserId, id));
        return this.removeById(id);
    }
}
