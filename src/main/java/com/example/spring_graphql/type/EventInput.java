package com.example.spring_graphql.type;

import lombok.Data;

@Data
public class EventInput {
    private String title;
    private String description;
    private Double price;
}
