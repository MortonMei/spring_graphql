package com.example.spring_graphql.fetcher;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.spring_graphql.entity.EventEntity;
import com.example.spring_graphql.mapper.EventEntityMapper;
import com.example.spring_graphql.type.Event;
import com.example.spring_graphql.type.EventInput;
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

    public EventDataFetcher(EventEntityMapper eventEntityMapper) {
        this.eventEntityMapper = eventEntityMapper;
    }

    @DgsQuery
    public List<Event> events() {
        List<EventEntity> eventEntityList = eventEntityMapper.selectList(new QueryWrapper<>());
        List<Event> eventList = eventEntityList.stream()
                .map(Event::fromEventEntity).collect(Collectors.toList());
        return eventList;
    }

    @DgsMutation
    public Event createEvent(@InputArgument(name = "eventInput") EventInput input) {
        EventEntity eventEntity = EventEntity.fromEventInput(input);
        eventEntityMapper.insert(eventEntity);

        return Event.fromEventEntity(eventEntity);
    }
}
