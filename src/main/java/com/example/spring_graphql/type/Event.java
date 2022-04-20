package com.example.spring_graphql.type;
import com.example.spring_graphql.entity.EventEntity;
import com.example.spring_graphql.util.DateUtil;
import lombok.Data;

@Data
public class Event {
    private String id;
    private String title;
    private String description;
    private Double price;
    private String date;

    public static Event fromEventEntity(EventEntity eventEntity) {
        Event event = new Event();
        event.setId(eventEntity.getId().toString());
        event.setDescription(eventEntity.getDescription());
        event.setTitle(eventEntity.getTitle());
        event.setPrice(eventEntity.getPrice());
        event.setDate(DateUtil.formatDateInISOString(eventEntity.getDate()));
        return event;

    }
}
