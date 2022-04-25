package com.example.spring_graphql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spring_graphql.entity.EventEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EventEntityMapper extends BaseMapper<EventEntity> {
}
