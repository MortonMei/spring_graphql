package com.example.spring_graphql.fetcher;

import com.example.spring_graphql.custom.AuthContext;
import com.example.spring_graphql.entity.BookingEntity;
import com.example.spring_graphql.entity.EventEntity;
import com.example.spring_graphql.entity.UserEntity;
import com.example.spring_graphql.mapper.BookingEntityMapper;
import com.example.spring_graphql.mapper.EventEntityMapper;
import com.example.spring_graphql.mapper.UserEntityMapper;
import com.example.spring_graphql.type.Booking;
import com.example.spring_graphql.type.Event;
import com.example.spring_graphql.type.User;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.context.DgsContext;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Morton.Mei
 * @Description: Booking
 * @Date 2022/4/24 17:34
 */
@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class BookingDataFetcher {
    private final BookingEntityMapper bookingEntityMapper;
    private final UserEntityMapper userEntityMapper;
    private final EventEntityMapper eventEntityMapper;

    @DgsQuery
    public List<Booking> bookings(DataFetchingEnvironment dfe) {
        AuthContext authContext = DgsContext.getCustomContext(dfe);
        authContext.ensureAuthenticated();

        List<BookingEntity> bookingEntityList = bookingEntityMapper.selectList(null);
        List<Booking> bookingList = bookingEntityList.stream()
                .map(Booking::fromBookingEntity).collect(Collectors.toList());
        return bookingList;
    }

    @DgsMutation
    public Booking bookEvent(@InputArgument String eventId, DataFetchingEnvironment dfe) {
        AuthContext authContext = DgsContext.getCustomContext(dfe);
        authContext.ensureAuthenticated();
        Integer userId = authContext.getUserEntity().getId();
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setEventId(Integer.valueOf(eventId));
        bookingEntity.setUserId(userId);
        bookingEntity.setCreateTime(new Date());
        bookingEntity.setUpdateTime(new Date());
        bookingEntityMapper.insert(bookingEntity);
        return Booking.fromBookingEntity(bookingEntity);
    }

    @DgsMutation
    public Event cancelBooking(@InputArgument(name = "bookingId") String bookingIdStr, DataFetchingEnvironment dfe) {
        AuthContext authContext = DgsContext.getCustomContext(dfe);
        authContext.ensureAuthenticated();
        Integer bookingId = Integer.parseInt(bookingIdStr);

        BookingEntity bookingEntity = bookingEntityMapper.selectById(bookingId);
        if(bookingEntity == null) {
            throw new RuntimeException(String.format("Booking with id %s does not exist", bookingIdStr));
        }
        if(!authContext.getUserEntity().getId().equals(bookingEntity.getUserId())) {
            throw new RuntimeException("You are not allowed delete other's booking");
        }
        Integer eventId = bookingEntity.getEventId();
        bookingEntityMapper.deleteById(bookingId);
        EventEntity eventEntity = eventEntityMapper.selectById(eventId);
        return Event.fromEventEntity(eventEntity);
    }

    @DgsData(parentType = "Booking", field = "user")
    public User user(DgsDataFetchingEnvironment dfe) {
        Booking booking = dfe.getSource();
        UserEntity userEntity = userEntityMapper.selectById(booking.getUserId());
        return User.fromUserEntity(userEntity);
    }

    @DgsData(parentType = "Booking", field = "event")
    public Event event(DgsDataFetchingEnvironment dfe) {
        Booking booking = dfe.getSource();
        EventEntity eventEntity = eventEntityMapper.selectById(booking.getEventId());
        return Event.fromEventEntity(eventEntity);
    }
}
