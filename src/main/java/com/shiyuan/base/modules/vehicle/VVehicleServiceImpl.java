package com.shiyuan.base.modules.vehicle;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.shiyuan.base.modules.vehicle.dto.VVehicleAddDTO;
import com.shiyuan.base.modules.vehicle.dto.VVehicleUpdateDTO;
import com.shiyuan.base.modules.vehicle.mapper.VVehicleMapper;
import com.shiyuan.base.modules.vehicle.vo.VVehicleDictVO;
import com.shiyuan.base.modules.vehicle.vo.VVehicleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author wangshiyuan
* @description 针对表【v_vehicle】的数据库操作Service实现
* @createDate 2025-04-24 13:40:42
*/
@Service
@Primary
public class VVehicleServiceImpl extends ServiceImpl<VVehicleMapper, VVehicle>
    implements VVehicleService{

    @Autowired
    VVehicleMapper vVehicleMapper;

    @Autowired
    private VehicleConverter vehicleConverter;

    @Transactional(readOnly = true)
    @Override
    public List<VVehicleDictVO> getDictVO(String blurry) {
        LambdaQueryWrapper<VVehicle> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(blurry)) {
            wrapper.like(VVehicle::getVehicleNo, blurry);
        }
        wrapper.eq(VVehicle::getState, 1);
        wrapper.select(VVehicle::getId, VVehicle::getVehicleNo, VVehicle::getProject, VVehicle::getPlace, VVehicle::getState);
        wrapper.orderByDesc(VVehicle::getCreateTime);

        List<VVehicle> vehicleList = this.list(wrapper);
        return vehicleConverter.toDictVOList(vehicleList);
    }

    @Transactional(readOnly = true)
    @Override
    public IPage<VVehicleVO> getVehiclePage(String blurry, Long currentPage, Long pageSize, String sort, String order) {
        Page<VVehicleVO> page = new Page<>(currentPage, pageSize);
        return vVehicleMapper.selectVehiclePage(page, blurry, sort, order);
    }

    @Transactional(readOnly = true)
    @Override
    public long getCount(Integer state) {
        LambdaQueryWrapper<VVehicle> wrapper = new LambdaQueryWrapper<>();
        if (state != null) {
            wrapper.eq(VVehicle::getState, state);
        }
        return this.count(wrapper);
    }

    @Transactional
    @Override
    public Long addVehicle(VVehicleAddDTO vehicleDTO) {
        VVehicle vehicle = vehicleConverter.toEntity(vehicleDTO);
        LambdaQueryWrapper<VVehicle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VVehicle::getVehicleNo, vehicle.getVehicleNo());
        if (this.exists(wrapper)) {
            throw new IllegalArgumentException("车辆编号已存在");
        }
        this.save(vehicle);
        return vehicle.getId();
    }

    @Transactional
    @Override
    public VVehicle updateVehicle(Long id, VVehicleUpdateDTO vehicle) {
        VVehicle existingVehicle = this.getById(id);
        if (existingVehicle == null) {
            throw new IllegalArgumentException("车辆不存在");
        }
        // 校验 vehicleNo 是否已存在（除自己以外）
        if (vehicle.getVehicleNo() != null) {
            boolean vehicleNoExists = this.lambdaQuery()
                    .eq(VVehicle::getVehicleNo, vehicle.getVehicleNo())
                    .ne(VVehicle::getId, id) // 排除自己
                    .exists();
            if (vehicleNoExists) {
                throw new IllegalArgumentException("车辆编号已存在");
            }
        }
        // 只复制有值的字段
        vehicleConverter.updateVehicleFromDto(vehicle, existingVehicle);
        this.updateById(existingVehicle);
        return existingVehicle;
    }
}




