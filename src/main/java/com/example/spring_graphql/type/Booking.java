package com.example.spring_graphql.type;

import com.example.spring_graphql.entity.BookingEntity;
import com.example.spring_graphql.util.DateUtil;
import lombok.Data;

/**
 * @Author Morton.Mei
 * @Description:
 * @Date 2022/4/24 17:26
 */
@Data
public class Booking {
    private Integer id;
    private Integer userId;
    private User user;
    private Integer eventId;
    private Event event;
    private String createTime;
    private String updateTime;

    public static Booking fromBookingEntity(BookingEntity bookingEntity) {
        Booking booking = new Booking();
        booking.setId(bookingEntity.getId());
        booking.setUserId(bookingEntity.getUserId());
        booking.setEventId(bookingEntity.getEventId());
        booking.setCreateTime(DateUtil.formatDateInISOString(bookingEntity.getCreateTime()));
        booking.setUpdateTime(DateUtil.formatDateInISOString(bookingEntity.getUpdateTime()));
        return booking;
    }
}
