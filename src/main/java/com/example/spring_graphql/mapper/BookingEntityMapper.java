package com.example.spring_graphql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spring_graphql.entity.BookingEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Morton.Mei
 * @Description: 数据访问mapper
 * @Date 2022/4/24 17:19
 */
@Mapper
public interface BookingEntityMapper extends BaseMapper<BookingEntity> {
}
