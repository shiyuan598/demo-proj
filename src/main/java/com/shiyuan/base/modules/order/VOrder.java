package com.shiyuan.base.modules.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @TableName v_order
 */
@TableName(value ="v_order")
@Data
public class VOrder {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer subscriber;

    private String subscribeNote;

    private Integer module;

    private Integer vehicleId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String address;

    private String purpose;

    private String route;

    private Integer trailer;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer approver;

    private Integer driver;

    private Integer state;

    private String comment;

    private Integer inclinationDriver;
}