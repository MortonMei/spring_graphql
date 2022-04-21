package com.example.spring_graphql.type;

import com.example.spring_graphql.entity.UserEntity;
import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String password;

    public static User fromUserEntity(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
//        user.setPassword(userEntity.getPassword());
        return user;
    }
}
