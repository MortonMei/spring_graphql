package com.example.spring_graphql.fetcher;
import com.example.spring_graphql.type.Event;
import com.example.spring_graphql.type.EventInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DgsComponent
public class EventDataFetcher {
    private List<Event> eventList = new ArrayList<>();
    @DgsQuery
    public List<Event> events() {
        return eventList;
    }

    @DgsMutation
    public Event createEvent(@InputArgument(name = "eventInput") EventInput input) {
        Event newEvent = new Event();
        newEvent.setId(UUID.randomUUID().toString());
        newEvent.setTitle(input.getTitle());
        newEvent.setDescription(input.getDescription());
        newEvent.setPrice(input.getPrice().floatValue());
        newEvent.setDate(input.getDate());
        eventList.add(newEvent);

        return newEvent;
    }
}
