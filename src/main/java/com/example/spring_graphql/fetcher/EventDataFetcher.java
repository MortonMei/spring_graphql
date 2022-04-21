package com.example.spring_graphql.fetcher;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.spring_graphql.entity.EventEntity;
import com.example.spring_graphql.entity.UserEntity;
import com.example.spring_graphql.mapper.EventEntityMapper;
import com.example.spring_graphql.mapper.UserEntityMapper;
import com.example.spring_graphql.type.Event;
import com.example.spring_graphql.type.EventInput;
import com.example.spring_graphql.type.User;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@DgsComponent
public class EventDataFetcher {
    private final EventEntityMapper eventEntityMapper;
    private final UserEntityMapper userEntityMapper;

    public EventDataFetcher(EventEntityMapper eventEntityMapper, UserEntityMapper userEntityMapper) {
        this.eventEntityMapper = eventEntityMapper;
        this.userEntityMapper = userEntityMapper;
    }

    @DgsQuery
    public List<Event> events() {
        List<EventEntity> eventEntityList = eventEntityMapper.selectList(new QueryWrapper<>());
        List<Event> eventList = eventEntityList.stream()
                .map(eventEntity -> {
                    Event event = Event.fromEventEntity(eventEntity);
                    fillEventCreator(event, eventEntity.getCreatorId());
                    return event;
                }).collect(Collectors.toList());
        return eventList;
    }

    @DgsMutation
    public Event createEvent(@InputArgument(name = "eventInput") EventInput input) {
        EventEntity eventEntity = EventEntity.fromEventInput(input);
        eventEntityMapper.insert(eventEntity);
        Event event = Event.fromEventEntity(eventEntity);
        fillEventCreator(event, eventEntity.getCreatorId());
        return event;
    }

    private void fillEventCreator(Event event, Integer userId) {
        UserEntity userEntity = userEntityMapper.selectById(userId);
        event.setCreator(User.fromUserEntity(userEntity));
    }
}
