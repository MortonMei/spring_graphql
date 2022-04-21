package com.example.spring_graphql.fetcher;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.spring_graphql.entity.UserEntity;
import com.example.spring_graphql.mapper.UserEntityMapper;
import com.example.spring_graphql.type.User;
import com.example.spring_graphql.type.UserInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class UserDataFetcher {
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;

    @DgsQuery
    public List<User> users() {
        List<UserEntity> userEntityList = userEntityMapper.selectList(new QueryWrapper<>());
        List<User> userList = userEntityList.stream()
                .map(User::fromUserEntity).collect(Collectors.toList());
        return userList;
    }

    @DgsMutation
    public User createUser(@InputArgument UserInput userInput) {
        ensureUserNotExists(userInput);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userInput.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userInput.getPassword()));

        userEntityMapper.insert(userEntity);
        User newUser = User.fromUserEntity(userEntity);
        return newUser;
    }

    public void ensureUserNotExists(UserInput userInput) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getEmail, userInput.getEmail());
        if (userEntityMapper.exists(queryWrapper)) {
            throw new RuntimeException("该email已经注册过！");
        }
    }
}
