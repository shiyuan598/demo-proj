package com.shiyuan.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.entity.Module;
import com.shiyuan.base.service.ModuleService;
import com.shiyuan.base.mapper.ModuleMapper;
import org.springframework.stereotype.Service;

/**
* @author wangshiyuan
* @description 针对表【v_module(所属模块)】的数据库操作Service实现
* @createDate 2025-04-22 16:42:19
*/
@Service
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, Module>
    implements ModuleService{

}




