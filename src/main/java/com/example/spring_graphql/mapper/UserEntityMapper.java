package com.example.spring_graphql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spring_graphql.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserEntityMapper extends BaseMapper<UserEntity> {
}
