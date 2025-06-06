package com.shiyuan.base.modules.vehicle;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyuan.base.modules.vehicle.dto.VVehicleAddDTO;
import com.shiyuan.base.modules.vehicle.dto.VVehicleUpdateDTO;
import com.shiyuan.base.modules.vehicle.vo.VVehicleDictVO;
import com.shiyuan.base.modules.vehicle.vo.VVehicleVO;
import java.util.List;

/**
 * @author wangshiyuan
 * @description 针对表【v_vehicle】的数据库操作Service
 * @createDate 2025-04-24 13:40:42
 */
public interface VVehicleService extends IService<VVehicle> {
    List<VVehicleDictVO> getDictVO(String blurry);

    IPage<VVehicleVO> getVehiclePage(String blurry, Long currentPage, Long pageSize, String sort, String order);

    long getCount(Integer state);

    Long addVehicle(VVehicleAddDTO vehicleAddDTO);

    VVehicle updateVehicle(Long id, VVehicleUpdateDTO vehicleUpdateDTO);
}
