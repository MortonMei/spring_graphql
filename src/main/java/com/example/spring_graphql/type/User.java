package com.example.spring_graphql.type;

import com.example.spring_graphql.entity.UserEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private Integer id;
    private String email;
    private String password;
    private List<Event> createdEvent = new ArrayList<>();

    public static User fromUserEntity(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());

//        user.setPassword(userEntity.getPassword());

        return user;
    }
}
