package com.shiyuan.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.entity.Project;
import com.shiyuan.base.service.ProjectService;
import com.shiyuan.base.mapper.ProjectMapper;
import org.springframework.stereotype.Service;

/**
* @author wangshiyuan
* @description 针对表【v_project(所属项目)】的数据库操作Service实现
* @createDate 2025-04-22 16:42:19
*/
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project>
    implements ProjectService{

}




