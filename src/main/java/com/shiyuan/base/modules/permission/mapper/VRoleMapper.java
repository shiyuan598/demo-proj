package com.shiyuan.base.modules.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiyuan.base.modules.permission.entity.VRole;
import org.apache.ibatis.annotations.Param;

/**
* @author wangshiyuan
* @description 针对表【v_role】的数据库操作Mapper
* @createDate 2025-04-29 17:17:05
* @Entity generator.domain.VRole
*/
public interface VRoleMapper extends BaseMapper<VRole> {
    String selectRoleCodeByUserId(@Param("userId") Long userId);
}




