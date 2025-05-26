package com.shiyuan.base.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiyuan.base.modules.user.VUser;
import com.shiyuan.base.modules.user.vo.VUserVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author wangshiyuan
 * @description 针对表【v_user】的数据库操作Mapper
 * @createDate 2025-04-24 13:40:42 @Entity com.shiyuan.base.modules.user.VUser
 */
public interface VUserMapper extends BaseMapper<VUser> {
    List<VUserVO> getDrivers(@Param("blurry") String blurry);
}
