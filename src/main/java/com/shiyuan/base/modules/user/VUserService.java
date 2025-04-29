package com.shiyuan.base.modules.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyuan.base.modules.user.dto.VUserAddDTO;
import com.shiyuan.base.modules.user.dto.VUserUpdateDTO;
import com.shiyuan.base.modules.user.vo.VUserVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author wangshiyuan
* @description 针对表【v_user】的数据库操作Service
* @createDate 2025-04-24 13:40:42
*/
public interface VUserService extends IService<VUser> {

    boolean forgetPassword(String username, String telephone, String newPassword);

    @Transactional
    List<VUserVO> getDrivers(String blurry);

    IPage<VUserVO> getUserPage(String blurry, long currentPage, long pageSize, String sort, String order);

    @Transactional
    Long addUser(VUserAddDTO userAddDTO);

    @Transactional
    VUserVO updateUser(long id, VUserUpdateDTO userUpdateDTO);
}
