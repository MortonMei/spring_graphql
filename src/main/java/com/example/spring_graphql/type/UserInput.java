package com.example.spring_graphql.type;

import lombok.Data;

@Data
public class UserInput {
    private String email;
    private String password;
}
