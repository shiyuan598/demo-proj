package com.shiyuan.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName v_order
 */
@TableName(value ="v_order")
@Data
public class Order {
    private Integer id;

    private Integer subscriber;

    private String subscribeNote;

    private Integer module;

    private Integer vehicleId;

    private Date startTime;

    private Date endTime;

    private String address;

    private String purpose;

    private String route;

    private Integer load;

    private Date updateTime;

    private Date createTime;

    private Integer approver;

    private Integer driver;

    private Integer state;

    private String comment;

    private Integer inclinationDriver;
}