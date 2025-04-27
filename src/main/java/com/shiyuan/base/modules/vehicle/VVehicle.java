package com.shiyuan.base.modules.vehicle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @TableName v_vehicle
 */
@TableName(value ="v_vehicle")
@Data
public class VVehicle {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String vehicleNo;

    private String project;

    private String place;

    private Integer state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String reason;

    private Integer maintainer;
}