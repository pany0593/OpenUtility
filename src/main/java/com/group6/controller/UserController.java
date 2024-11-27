package com.group6.controller;

import com.group6.response.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.group6.pojo.User;
import com.group6.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public Base<User> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return new Base<>(200, "User registered successfully", user);
        } catch (IllegalArgumentException e) {
            return new Base<>(400, e.getMessage(), null);
        }
    }


    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public Base<String> loginUser(@RequestBody User user) {
        try {
            String token = userService.loginUser(user.getUsername(), user.getPassword());
            return new Base<>(200, "Login successful", token);
        } catch (IllegalArgumentException e) {
            return new Base<>(401, e.getMessage(), null);
        }
    }


    /**
     * 获取用户信息接口
     */
    @GetMapping("/{userId}/profile")
    public Base<User> getUserProfile(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return new Base<>(200, "User profile fetched successfully", user);
        } else {
            return new Base<>(404, "User not found", null);
        }
    }

    /**
     * 更新用户头像接口
     */
    @PostMapping("/{userId}/avatar")
    public Base<String> updateUserAvatar(@PathVariable Long userId, @RequestParam("avatar") String avatar) {
        try {
            userService.updateUserAvatar(userId, avatar);
            return new Base<>(200, "Avatar updated successfully", null);
        } catch (IllegalArgumentException e) {
            return new Base<>(400, e.getMessage(), null);
        }
    }

}
