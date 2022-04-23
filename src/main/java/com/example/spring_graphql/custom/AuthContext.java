package com.example.spring_graphql.custom;

import com.example.spring_graphql.entity.UserEntity;
import lombok.Data;

/**
 * @Author Morton.Mei
 * @Description: 用户认证上下文信息
 * @Date 2022/4/22 18:56
 */
@Data
public class AuthContext {
    private UserEntity userEntity;
    private boolean tokenInvalid;

    public void ensureAuthenticated() {
        if(tokenInvalid) {
            throw new RuntimeException("令牌无效！");
        }

        if(userEntity == null) {
            throw new RuntimeException("未登录，请先登录！");
        }

    }

}
