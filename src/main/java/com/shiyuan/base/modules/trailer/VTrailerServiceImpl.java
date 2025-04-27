package com.shiyuan.base.modules.trailer;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
* @author wangshiyuan
* @description 针对表【v_trailer(车辆带挂)】的数据库操作Service实现
* @createDate 2025-04-24 13:40:42
*/
@Service
@Primary
public class VTrailerServiceImpl extends ServiceImpl<VTrailerMapper, VTrailer>
    implements VTrailerService{

}




