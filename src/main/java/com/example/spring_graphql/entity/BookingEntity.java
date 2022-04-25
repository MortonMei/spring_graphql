package com.example.spring_graphql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author Morton.Mei
 * @Description: booking entity
 * @Date 2022/4/24 17:15
 */
@Data
@TableName(value = "tb_booking")
public class BookingEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer eventId;
    private Date createTime;
    private Date updateTime;
}
