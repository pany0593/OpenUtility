package com.group6.controller;

import org.springframework.web.bind.annotation.*;
import com.group6.pojo.User;

@RestController
@RequestMapping("/users")

public class UserController {
    /**
     * 用户注册接口
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {

        return null;
    }

    /**
     * 用户登录接口
     *
     */
    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {

        return null;
    }

    /**
     * 获取用户信息接口
     *
     * @param userId
     * @return null
     */
    @GetMapping("/{userId}/profile")
    public String getUserProfile(@PathVariable Long userId) {

        return null;
    }

    /**
     * 更新用户头像接口
     *
     */
    @PostMapping("/{userId}/avatar")
    public String updateUserAvatar(@PathVariable Long userId, @RequestParam("avatar") String avatar) {

        return null;
    }
}
