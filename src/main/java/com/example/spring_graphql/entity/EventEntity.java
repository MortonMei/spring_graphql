package com.example.spring_graphql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.spring_graphql.type.EventInput;
import com.example.spring_graphql.util.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "event")
public class EventEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String description;
    private Double price;
    private Date date;
    private Integer creatorId;

    public static EventEntity fromEventInput(EventInput input) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setTitle(input.getTitle());
        eventEntity.setDescription(input.getDescription());
        eventEntity.setPrice(input.getPrice());
        eventEntity.setDate(DateUtil.convertISOStringToDate(input.getDate()));
        eventEntity.setCreatorId(input.getCreatorId());
        return eventEntity;
    }

}
