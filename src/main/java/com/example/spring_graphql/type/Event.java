package com.example.spring_graphql.type;
import com.example.spring_graphql.entity.EventEntity;
import com.example.spring_graphql.util.DateUtil;
import lombok.Data;

@Data
public class Event {
    private Integer id;
    private String title;
    private String description;
    private Double price;
    private String date;
    private Integer creatorId;
    private User creator;

    public static Event fromEventEntity(EventEntity eventEntity) {
        Event event = new Event();
        event.setId(eventEntity.getId());
        event.setDescription(eventEntity.getDescription());
        event.setTitle(eventEntity.getTitle());
        event.setPrice(eventEntity.getPrice());
        event.setDate(DateUtil.formatDateInISOString(eventEntity.getDate()));
        event.setCreatorId(eventEntity.getCreatorId());
        return event;

    }
}
