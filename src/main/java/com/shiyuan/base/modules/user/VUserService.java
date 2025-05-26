package com.shiyuan.base.modules.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyuan.base.modules.user.dto.VUserAddDTO;
import com.shiyuan.base.modules.user.dto.VUserUpdateDTO;
import com.shiyuan.base.modules.user.vo.VUserVO;
import java.util.List;

/**
 * @author wangshiyuan
 * @description 针对表【v_user】的数据库操作Service
 * @createDate 2025-04-24 13:40:42
 */
public interface VUserService extends IService<VUser> {

    boolean forgetPassword(String username, String telephone, String newPassword);

    List<VUserVO> getDrivers(String blurry);

    IPage<VUserVO> getUserPage(String blurry, Long currentPage, Long pageSize, String sort, String order);

    Long addUser(VUserAddDTO userAddDTO);

    VUserVO updateUser(Long id, VUserUpdateDTO userUpdateDTO);

    boolean removeUserById(Long id);
}
