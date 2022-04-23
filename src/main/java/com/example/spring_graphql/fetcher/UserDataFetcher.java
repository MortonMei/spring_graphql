package com.example.spring_graphql.fetcher;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.spring_graphql.custom.AuthContext;
import com.example.spring_graphql.entity.EventEntity;
import com.example.spring_graphql.entity.UserEntity;
import com.example.spring_graphql.mapper.EventEntityMapper;
import com.example.spring_graphql.mapper.UserEntityMapper;
import com.example.spring_graphql.type.AuthData;
import com.example.spring_graphql.type.Event;
import com.example.spring_graphql.type.LoginInput;
import com.example.spring_graphql.type.User;
import com.example.spring_graphql.type.UserInput;
import com.example.spring_graphql.util.TokenUtil;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.context.DgsContext;
import graphql.schema.DataFetchingEnvironment;
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
    public AuthData login(@InputArgument LoginInput loginInput) {
        UserEntity userEntity = this.findUserByEmail(loginInput.getEmail());
        if(userEntity == null) {
            throw new RuntimeException("该Email未注册");
        }
        Boolean match = passwordEncoder.matches(loginInput.getPassword(), userEntity.getPassword());
        if(!match) {
            throw new RuntimeException("密码不正确");
        }
        String token = TokenUtil.signToken(userEntity.getId(), 1);
        AuthData authData = new AuthData()
                .setUserId(userEntity.getId())
                .setToken(token)
                .setTokenExpiration(1);
        return authData;
    }

    /**
     * @Author Morton.Mei
     * @Description: query user list
     * @Date 18:46 2022/4/22
     */
    @DgsQuery
    public List<User> users(DataFetchingEnvironment dfe) {
        AuthContext authContext = DgsContext.getCustomContext(dfe);
        authContext.ensureAuthenticated();

        List<UserEntity> userEntityList = userEntityMapper.selectList(new QueryWrapper<>());
        List<User> userList = userEntityList.stream()
                .map(User::fromUserEntity).collect(Collectors.toList());
        return userList;
    }


    @DgsMutation
    public User createUser(@InputArgument UserInput userInput) {
//      确认用户是否已注册
        if(findUserByEmail(userInput.getEmail()) != null) {
            throw new RuntimeException("该email已经注册过！");
        }

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

    private UserEntity findUserByEmail(String email) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getEmail, email);
        return userEntityMapper.selectOne(queryWrapper);
    }
}
