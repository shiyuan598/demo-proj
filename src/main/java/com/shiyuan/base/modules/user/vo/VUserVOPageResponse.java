package com.shiyuan.base.modules.user.vo;

import com.shiyuan.base.common.response.BasePageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户分页列表响应对象")
public class VUserVOPageResponse extends BasePageResponse<VUserVO> {
}
