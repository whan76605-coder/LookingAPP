package com.xhsapp.service;

import com.xhsapp.entity.User;

public interface UserService {

    User register(String username, String password, String nickname);

    User login(String username, String password);

    User findByUsername(String username);
}
