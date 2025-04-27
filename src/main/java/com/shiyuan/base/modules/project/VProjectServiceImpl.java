package com.shiyuan.base.modules.project;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.modules.project.mapper.VProjectMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
* @author wangshiyuan
* @description 针对表【v_project(所属项目)】的数据库操作Service实现
* @createDate 2025-04-24 13:40:42
*/
@Service
@Primary
public class VProjectServiceImpl extends ServiceImpl<VProjectMapper, VProject>
    implements VProjectService{

}




