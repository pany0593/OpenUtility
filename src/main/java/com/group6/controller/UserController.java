package com.group6.controller;

import com.group6.pojo.Result;
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
    public Result registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return Result.success(user);
    }


    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public Result loginUser(@RequestBody User user) {
        try {
            String token = userService.loginUser(user.getUsername(), user.getPassword());
            return Result.success(token);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 获取用户信息接口
     */
    @GetMapping("/{userId}/profile")
    public Result getUserProfile(@PathVariable String userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("user not found");
        }
    }

    /**
     * 用户头像接口
     */
    @PostMapping("/{userId}/avatar")
    public Result updateUserAvatar(@PathVariable String userId, @RequestParam("avatar") String avatar) {
        try {
            userService.updateUserAvatar(userId, avatar);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

}
