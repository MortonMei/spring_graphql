package com.example.spring_graphql.type;

import lombok.Data;

/**
 * @Author Morton.Mei
 * @Description: login input
 * @Date 2022/4/22 17:39
 */
@Data
public class LoginInput {
    private String email;
    private String password;
}
