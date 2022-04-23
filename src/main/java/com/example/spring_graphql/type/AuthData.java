package com.example.spring_graphql.type;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author Morton.Mei
 * @Description: login return
 * @Date 2022/4/22 17:40
 */
@Data
@Accessors(chain = true)
public class AuthData {
    private Integer userId;
    private String token;
    private Integer tokenExpiration;
}
