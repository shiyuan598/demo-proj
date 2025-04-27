package com.shiyuan.base.modules.vehicle;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyuan.base.modules.vehicle.vo.VVehicleDictVO;
import com.shiyuan.base.modules.vehicle.vo.VVehicleVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author wangshiyuan
* @description 针对表【v_vehicle】的数据库操作Service
* @createDate 2025-04-24 13:40:42
*/
public interface VVehicleService extends IService<VVehicle> {
    List<VVehicleDictVO> getDictVO(String blurry);

    @Transactional(readOnly = true)
    IPage<VVehicleVO> getVehiclePage(String blurry, long currentPage, long pageSize, String sort, String order);
}
