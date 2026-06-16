package com.xhsapp.controller;

import com.xhsapp.common.Result;
import com.xhsapp.entity.User;
import com.xhsapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** POST /api/v1/auth/register */
    @PostMapping("/register")
    public Result<User> register(@RequestBody Map<String, String> body) {
        try {
            User user = userService.register(
                    body.get("username"),
                    body.get("password"),
                    body.get("nickname"));
            return Result.ok(user);
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

    /** POST /api/v1/auth/login */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String, String> body) {
        try {
            User user = userService.login(
                    body.get("username"),
                    body.get("password"));
            return Result.ok(user);
        } catch (RuntimeException e) {
            return Result.fail(401, e.getMessage());
        }
    }
}
