package com.shiyuan.base.modules.module;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author wangshiyuan
 * @description 针对表【v_module(所属模块)】的数据库操作Service实现
 * @createDate 2025-04-24 13:40:41
 */
@Service
@Primary
public class VModuleServiceImpl extends ServiceImpl<VModuleMapper, VModule>
        implements VModuleService{

}