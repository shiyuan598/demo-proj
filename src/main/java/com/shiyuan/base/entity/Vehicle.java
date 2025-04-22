package com.shiyuan.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName v_vehicle
 */
@TableName(value ="v_vehicle")
@Data
public class Vehicle {
    private Integer id;

    private String vehicleNo;

    private String project;

    private String place;

    private Integer state;

    private Date createTime;

    private String reason;

    private Integer maintainer;
}