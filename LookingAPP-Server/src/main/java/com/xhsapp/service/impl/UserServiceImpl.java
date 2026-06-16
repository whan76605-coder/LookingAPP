package com.xhsapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xhsapp.entity.User;
import com.xhsapp.mapper.UserMapper;
import com.xhsapp.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User register(String username, String password, String nickname) {
        // 用户名唯一检查
        User exist = findByUsername(username);
        if (exist != null) {
            throw new RuntimeException("用户名已被注册");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(md5(password));
        user.setNickname(nickname != null ? nickname : username);
        user.setAvatar("");
        user.setBio("热爱生活，热爱旅行");
        user.setFollowCount(0);
        user.setFansCount(0);
        user.setLikeCount(0);
        user.setCreateTime(System.currentTimeMillis());

        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = findByUsername(username);
        if (user == null || !md5(password).equals(user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    private static String md5(String s) {
        return DigestUtils.md5DigestAsHex(s.getBytes(StandardCharsets.UTF_8));
    }
}
