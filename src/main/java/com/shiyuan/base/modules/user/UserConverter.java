package com.shiyuan.base.modules.user;


import com.shiyuan.base.modules.user.dto.VUserAddDTO;
import com.shiyuan.base.modules.user.dto.VUserUpdateDTO;
import com.shiyuan.base.modules.user.vo.VUserVO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter {
    // DTO 转 Entity
    VUser toEntity(VUserAddDTO userDTO);

    // Entity 转 VO
    VUserVO toVO(VUser user);

    // Entity 列表转 VO 列表
    List<VUserVO> toVOList(List<VUser> users);

    // 部分更新 Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(VUserUpdateDTO userDTO, @MappingTarget VUser user);
}
