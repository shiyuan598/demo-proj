package com.shiyuan.base.entity.converter;


import com.shiyuan.base.entity.VUser;
import com.shiyuan.base.entity.dto.VUserDTO;
import com.shiyuan.base.entity.vo.user.VUserVO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter {
    // DTO 转 Entity
    @Mapping(target = "token", ignore = true)
    VUser toEntity(VUserDTO userDTO);

    // Entity 转 VO
    VUserVO toVO(VUser user);

    // Entity 列表转 VO 列表
    List<VUserVO> toVOList(List<VUser> users);

    // 部分更新 Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(VUserDTO userDTO, @MappingTarget VUser user);
}
