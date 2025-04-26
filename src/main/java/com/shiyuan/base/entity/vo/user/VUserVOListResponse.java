package com.shiyuan.base.entity.vo.user;

import com.shiyuan.base.entity.vo.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "用户列表响应对象")
public class VUserVOListResponse extends BaseResponse<List<VUserVO>> {
}
