package com.example.spring_graphql.fetcher;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.spring_graphql.entity.EventEntity;
import com.example.spring_graphql.entity.UserEntity;
import com.example.spring_graphql.mapper.EventEntityMapper;
import com.example.spring_graphql.mapper.UserEntityMapper;
import com.example.spring_graphql.type.Event;
import com.example.spring_graphql.type.User;
import com.example.spring_graphql.type.UserInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
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
    private final EventEntityMapper eventEntityMapper;
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

    /**
     * @Author Morton.Mei
     * @Description: "createdEvents" object resolver at User
     * @Date 20:33 2022/4/21
     */
    @DgsData(parentType = "User", field = "createdEvents")
    public List<Event> createdEvents(DgsDataFetchingEnvironment dgf) {
        User user = dgf.getSource();
        QueryWrapper<EventEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EventEntity::getCreatorId, user.getId());
        List<EventEntity> eventEntityList = eventEntityMapper.selectList(queryWrapper);
        List<Event> eventList = eventEntityList.stream()
                .map(Event::fromEventEntity).collect(Collectors.toList());
        return eventList;
    }

    public void ensureUserNotExists(UserInput userInput) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getEmail, userInput.getEmail());
        if (userEntityMapper.exists(queryWrapper)) {
            throw new RuntimeException("该email已经注册过！");
        }
    }
}
