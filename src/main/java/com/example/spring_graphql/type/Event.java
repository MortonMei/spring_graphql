package com.example.spring_graphql.type;
import lombok.Data;

@Data
public class Event {
    private String id;
    private String title;
    private String description;
    private Float price;
    private String date;
}
