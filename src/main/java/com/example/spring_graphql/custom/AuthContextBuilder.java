package com.example.spring_graphql.custom;

import com.example.spring_graphql.entity.UserEntity;
import com.example.spring_graphql.mapper.UserEntityMapper;
import com.example.spring_graphql.util.TokenUtil;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.context.DgsCustomContextBuilderWithRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * @Author Morton.Mei
 * @Description: 设置认证上下文
 * @Date 2022/4/22 21:21
 */
@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class AuthContextBuilder implements DgsCustomContextBuilderWithRequest {
    static final String AUTHORIZATION = "Authorization";
    private final UserEntityMapper userEntityMapper;

    @Override
    public Object build(@Nullable Map map, @Nullable HttpHeaders httpHeaders, @Nullable WebRequest webRequest) {
        log.info("Building Auth Context ...");
        AuthContext authContext = new AuthContext();
        if(!httpHeaders.containsKey(AUTHORIZATION)) {
            log.info("用户未认证！");
            return authContext;
        }
        String authorization = httpHeaders.getFirst(AUTHORIZATION);
        String token = authorization.replace("Bearer ", "");
        Integer userId;
        try {
            userId = TokenUtil.verifyToken(token);
        } catch (Exception e) {
            authContext.setTokenInvalid(true);
            return authContext;
        }
        UserEntity userEntity = userEntityMapper.selectById(userId);
        if(userEntity == null) {
            authContext.setTokenInvalid(true);
        }
        authContext.setUserEntity(userEntity);
        log.info("用户认证成功， userId = {}", userId);
        return authContext;
    }
}
