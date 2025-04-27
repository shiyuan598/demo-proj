package com.shiyuan.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyuan.base.entity.VVehicle;
import com.shiyuan.base.entity.vo.vehicle.VVehicleVO;
import org.apache.ibatis.annotations.Param;

/**
* @author wangshiyuan
* @description 针对表【v_vehicle】的数据库操作Mapper
* @createDate 2025-04-24 13:40:42
* @Entity com.shiyuan.base.entity.VVehicle
*/
public interface VVehicleMapper extends BaseMapper<VVehicle> {
    IPage<VVehicleVO> selectVehiclePage(Page<VVehicleVO> page, @Param("blurry") String blurry, @Param("sort") String sort, @Param("order") String order);

}




